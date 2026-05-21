# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

- `npm run watch` — dev server (proxies `/v1` and `/Shibboleth.sso/*` to production so login works locally).
- `npm run build` — production build into `code/static/`.
- `npm run clean` — remove `code/static/`.

No tests or linter configured.

## Architecture

A virtual student card for the EC2U Alliance, built on the European Student Card (ESC) framework and eduGAIN SSO.

Three pieces in one repo:

- **`code/javascript/`** — React SPA (Vite-built). Renders the card at `/` and markdown pages elsewhere.
- **`code/php/`** — `/v1` endpoint behind Shibboleth: reads eduGAIN attributes, looks up the user's institution in a
  tenants config, calls the central ESC API, returns `{user, cards}`.
- **`code/apache/`, `code/shibboleth/`** — reference deployment config.

No personal data is stored server-side; everything is fetched live from ESC per request.

Path alias `@ec2u/card/*` → `code/javascript/*` (see `tsconfig.json` / `vite.config.js`).
