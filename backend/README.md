# GDSK backend setup (first deployment)
This repository contains the Android app and a Cloudflare Worker API skeleton.
## Database
Create a Cloudflare D1 database and run `backend/schema.sql`.
Bind it to the Worker as `DB`.
## Worker secret
Set `ADMIN_API_KEY` as a Worker secret. Deploy `backend/worker.js`.
## API
- GET /health
- GET /api/events?group=S-31
- POST /api/events (Authorization: Bearer ADMIN_API_KEY)
- POST /api/register (FCM device-token storage endpoint; Firebase client integration is still required)
## Push notifications
True background push requires a Firebase project, Android app registration for package `by.gdsk.app`, the generated `google-services.json`, Firebase Messaging SDK, and server-side FCM credentials. None of those account-owned credentials can be generated without access to the owner's Google/Firebase account. Until that setup is completed, local Android alarms work only on the device where an event was added; do not describe them as broadcasts to all students.
