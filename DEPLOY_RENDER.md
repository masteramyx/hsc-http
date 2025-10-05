# Deploy HSC HTTP to Render

## Overview
This guide will deploy your full stack (React frontend + Ktor backend + PostgreSQL) to Render.

## Prerequisites
- GitHub account with your code pushed
- Render account (sign up at https://render.com)

---

## Step 1: Push Your Code to GitHub

```bash
git add .
git commit -m "Prepare for Render deployment"
git push origin trunk
```

---

## Step 2: Create Render Account

1. Go to https://render.com
2. Click "Get Started for Free"
3. Sign up with GitHub
4. Authorize Render to access your repositories

---

## Step 3: Deploy Using render.yaml (Automatic)

### Option A: Blueprint (Easiest)

1. In Render Dashboard, click **"New +"** → **"Blueprint"**
2. Connect your GitHub repository `hsc-http`
3. Render will detect `render.yaml` and create:
   - PostgreSQL database
   - Web service (Ktor backend + React frontend)
4. Click **"Apply"**
5. Wait 5-10 minutes for build to complete

### Option B: Manual Setup

If Blueprint doesn't work, follow these steps:

#### 3a. Create PostgreSQL Database

1. Dashboard → **"New +"** → **"PostgreSQL"**
2. Settings:
   - **Name**: `hsc-http-db`
   - **Database**: `healthshadow_dev`
   - **User**: `hsc_user`
   - **Region**: Oregon (US West) or closest to you
   - **PostgreSQL Version**: 15
   - **Plan**: Free
3. Click **"Create Database"**
4. **Important**: Copy the **Internal Database URL** (you'll need this)

#### 3b. Deploy Backend (with React Frontend)

1. Dashboard → **"New +"** → **"Web Service"**
2. Connect your GitHub repository
3. Settings:
   - **Name**: `hsc-http-backend`
   - **Region**: Same as database
   - **Branch**: `trunk`
   - **Root Directory**: Leave empty
   - **Runtime**: **Docker**
   - **Plan**: Free
4. **Environment Variables** (click "Add Environment Variable"):
   - `DATABASE_URL`: Paste the Internal Database URL from step 3a
   - `PORT`: `8080`
5. **Health Check Path**: `/health`
6. Click **"Create Web Service"**

---

## Step 4: Wait for Deployment

- First build takes ~10-15 minutes (building Docker image)
- Watch the logs in Render dashboard
- Look for: "Application started"

---

## Step 5: Access Your Site

Once deployed, Render gives you a URL like:
```
https://hsc-http-backend.onrender.com
```

Your React landing page will be served at the root URL.

---

## Step 6: Set Up Custom Domain (shadowconnects.com)

### In Render Dashboard:

1. Go to your web service → **"Settings"** tab
2. Scroll to **"Custom Domains"** section
3. Click **"Add Custom Domain"**
4. Add both:
   - `shadowconnects.com`
   - `www.shadowconnects.com`

### Update DNS at Your Domain Registrar:

Render will provide DNS records. Add these where you bought your domain:

**For root domain (`shadowconnects.com`):**
- **Type**: `A` record
- **Name**: `@`
- **Value**: Render's IP address (provided in dashboard)

**For www subdomain (`www.shadowconnects.com`):**
- **Type**: `CNAME` record
- **Name**: `www`
- **Value**: `hsc-http-backend.onrender.com` (or whatever Render shows)

### SSL Certificate:

- Render automatically provisions **free SSL** (HTTPS)
- Takes 5-60 minutes after DNS propagates
- Auto-renews forever

### Result:

- ✅ `https://shadowconnects.com` → Your landing page
- ✅ `https://www.shadowconnects.com` → Your landing page
- ✅ Free SSL/HTTPS

---

## Troubleshooting

### Build Fails
- Check logs in Render dashboard
- Ensure all files are committed to GitHub
- Verify `Dockerfile` is correct

### Database Connection Issues
- Ensure `DATABASE_URL` environment variable is set correctly
- Use the **Internal Database URL**, not external
- Check database is in same region as web service

### Frontend Not Loading
- Verify React build succeeded: Check logs for "react-web:assemble"
- Ensure `/health` endpoint returns 200 OK
- Check browser console for errors

### Custom Domain Not Working
- Wait 5-60 minutes for DNS propagation
- Verify DNS records are correct at your registrar
- Use tools like `dig shadowconnects.com` or https://dnschecker.org to verify

---

## Environment Variables Reference

| Variable | Value | Notes |
|----------|-------|-------|
| `DATABASE_URL` | From Render PostgreSQL | Auto-provided by Render |
| `PORT` | `8080` | Required by Render |

---

## Free Tier Limits

**PostgreSQL Database (Free):**
- 256 MB RAM
- 1 GB Storage
- Expires after 90 days (then need to upgrade)

**Web Service (Free):**
- 512 MB RAM
- Spins down after 15 minutes of inactivity
- Spins up on first request (30s-1min delay)

**Upgrade to Paid ($7/month per service):**
- Always on (no spin down)
- More resources
- Database doesn't expire

---

## Monitoring

- **Health Check**: `https://shadowconnects.com/health`
- **Logs**: Render Dashboard → Your Service → Logs tab
- **Metrics**: Render Dashboard → Your Service → Metrics tab

---

## Updating Your App

Push to GitHub, Render auto-deploys:

```bash
git add .
git commit -m "Update landing page"
git push origin trunk
```

Render automatically rebuilds and redeploys in ~5 minutes.

---

## Next Steps

1. ✅ Deploy to Render
2. ✅ Test your landing page at temporary URL
3. ✅ Connect shadowconnects.com custom domain
4. Add monitoring/alerts
5. Consider upgrading for production traffic (no spin down)
6. Set up CI/CD for automated testing before deploy
