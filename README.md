# HAO — API Search

A small HAO app for searching supported identifiers through a server-side lookup service.

## Run locally
Requires Node.js 18+.

```bash
npm start
```

Open http://localhost:3000

## Vercel
Import this repository into Vercel. No build command is required.

Configure the lookup service using Vercel environment variables. Keep upstream service URLs, paths, and API keys server-side; do not put them in the Android app or public documentation.

Use synthetic/test identifiers only.
