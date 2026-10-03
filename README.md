# One for All

A learning-focused tools hub inspired by the focused workflow/UI patterns in chatpataprani/Reeldrop.

## Included
- Responsive YAWR/Reeldrop-inspired shell.
- 26 tool entries derived from the supplied punjab.pages.dev-main.zip.
- DB Lab UI for your own Both-db test API.
- Server-side /api/lookup proxy so the upstream API key is not shipped to the browser.
- Vercel function configuration.

## Both-db configuration
Do not put the secret in app.js, HTML, or any public file.

Set these server environment variables:
- BOTH_DB_API_URL — your fake/test Both-db API base URL.
- BOTH_DB_API_KEY — your API secret.
- BOTH_DB_NUMBER_PATH — optional number lookup path; defaults to /api/number.
- BOTH_DB_AADHAAR_PATH — optional Aadhaar lookup path; defaults to /api/aadhaar.

The frontend sends { "kind": "number" | "aadhar", "value": "..." } to /api/lookup. The proxy adds the secret server-side and forwards the request.

Use synthetic test identifiers while learning.

## Deployment
This is ready for a serverless deployment such as Vercel. Add environment variables in the deployment platform server settings, not in GitHub source.

GitHub Actions secrets are available to workflows, not directly to browser JavaScript. If you deploy to GitHub Pages, use a separate backend/serverless function for /api/lookup.

## Punjab static bundle
The original ZIP was validated locally: all 17 JavaScript files passed node --check, and the extracted site contains 57 HTML screens. The full static bundle is large; keep it in public/punjab/ when uploading the local build, or point the tool viewer at your own deployment of the extracted ZIP.

## Run locally
python3 -m http.server 8080

For the API function, use a serverless runtime that supports api/lookup.js.
