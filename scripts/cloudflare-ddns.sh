#!/bin/bash

# Cloudflare Dynamic DNS Update Script
# Updates A record for shadowconnects.com with current public IP

# Configuration - SET THESE ENVIRONMENT VARIABLES
# export CLOUDFLARE_API_TOKEN="your_token_here"
# export ZONE_NAME="shadowconnects.com"

ZONE_NAME="${ZONE_NAME:-shadowconnects.com}"
RECORD_NAME="${RECORD_NAME:-shadowconnects.com}"  # Root domain

# Script variables
LOGFILE="/tmp/cloudflare-ddns.log"
CURRENT_IP_FILE="/tmp/current_ip"

# Function to log messages
log_message() {
    echo "$(date '+%Y-%m-%d %H:%M:%S') - $1" | tee -a "$LOGFILE"
}

# Check if required variables are set
if [ -z "$CLOUDFLARE_API_TOKEN" ]; then
    log_message "ERROR: CLOUDFLARE_API_TOKEN environment variable not set"
    exit 1
fi

# Get current public IP
CURRENT_IP=$(curl -s https://ipv4.icanhazip.com)
if [ -z "$CURRENT_IP" ]; then
    log_message "ERROR: Could not determine current public IP"
    exit 1
fi

# Check if IP has changed
if [ -f "$CURRENT_IP_FILE" ]; then
    LAST_IP=$(cat "$CURRENT_IP_FILE")
    if [ "$CURRENT_IP" = "$LAST_IP" ]; then
        log_message "INFO: IP unchanged ($CURRENT_IP), no update needed"
        exit 0
    fi
fi

log_message "INFO: IP changed from ${LAST_IP:-unknown} to $CURRENT_IP"

# Get Zone ID
ZONE_ID=$(curl -s -X GET "https://api.cloudflare.com/client/v4/zones?name=$ZONE_NAME" \
    -H "Authorization: Bearer $CLOUDFLARE_API_TOKEN" \
    -H "Content-Type: application/json" | \
    jq -r '.result[0].id')

if [ "$ZONE_ID" = "null" ] || [ -z "$ZONE_ID" ]; then
    log_message "ERROR: Could not get Zone ID for $ZONE_NAME"
    exit 1
fi

# Get Record ID
RECORD_ID=$(curl -s -X GET "https://api.cloudflare.com/client/v4/zones/$ZONE_ID/dns_records?name=$RECORD_NAME&type=A" \
    -H "Authorization: Bearer $CLOUDFLARE_API_TOKEN" \
    -H "Content-Type: application/json" | \
    jq -r '.result[0].id')

if [ "$RECORD_ID" = "null" ] || [ -z "$RECORD_ID" ]; then
    log_message "ERROR: Could not get Record ID for $RECORD_NAME"
    exit 1
fi

# Update DNS record
RESPONSE=$(curl -s -X PUT "https://api.cloudflare.com/client/v4/zones/$ZONE_ID/dns_records/$RECORD_ID" \
    -H "Authorization: Bearer $CLOUDFLARE_API_TOKEN" \
    -H "Content-Type: application/json" \
    --data "{\"type\":\"A\",\"name\":\"$RECORD_NAME\",\"content\":\"$CURRENT_IP\",\"ttl\":300}")

SUCCESS=$(echo "$RESPONSE" | jq -r '.success')

if [ "$SUCCESS" = "true" ]; then
    log_message "SUCCESS: Updated $RECORD_NAME to $CURRENT_IP"
    echo "$CURRENT_IP" > "$CURRENT_IP_FILE"
else
    ERROR=$(echo "$RESPONSE" | jq -r '.errors[0].message')
    log_message "ERROR: Failed to update DNS record - $ERROR"
    exit 1
fi