# HAO — API Search

A small HAO app that searches the provided **test/learning Both-db API**.

## API endpoints
- Number: https://both-db.vercel.app/number=
- Aadhaar: https://both-db.vercel.app/aadhar=

The browser calls `/api/lookup`, and the server calls Both-db. A future API key can therefore stay in a server environment variable.

## Run locally
Requires Node.js 18+.

```bash
npm start
```

Open http://localhost:3000

## Vercel
Import this repository into Vercel. No build command is required.

Optional environment variables:
- `BOTH_DB_API_URL=https://both-db.vercel.app`
- `BOTH_DB_NUMBER_PATH=/number=`
- `BOTH_DB_AADHAAR_PATH=/aadhar=`
- `BOTH_DB_API_KEY=...` only if required later.

Use synthetic/test identifiers only.
