# apl-barbershop

Sistema de gestão da JR Barbershop desenvolvido com React, Vite e Tailwind CSS.

## Development Server

A Vite development server can be started with `npm run dev` on `$PORT` (default 5173).

- Preview URL: `http://localhost:5173`
- Hot reload: Changes to source files are reflected immediately

## Project Structure

This is the canonical project structure. Start with task-relevant files below. Only follow imports or inspect other files when required, when a documented path is missing, or when the repository contradicts this guide.

- `src/main.jsx` - React entrypoint; imports the global styles and mounts the application into `#root`
- `src/app/App.jsx` - Primary application component
- `src/app/styles/index.css` - Global CSS entrypoint, design tokens, and Tailwind CSS v4 import
- `index.html` - Vite HTML shell containing metadata and the `#root` element
- `package.json` - Project dependencies and the Vite build, development, preview, and formatting scripts
- `vite.config.ts` - Vite configuration with React, Tailwind CSS v4, and the `@` alias for `src`
- `.mise.toml` - Node.js toolchain version; npm is the project package manager

## Dependencies

- Runtime: React 19 and React DOM 19
- Styling: Tailwind CSS v4 with the `@tailwindcss/vite` plugin
- Build tooling: Vite 8, TypeScript 5.7, and `@vitejs/plugin-react`
- Formatting: oxfmt

## Styling

This project uses **Tailwind CSS v4** through the `@tailwindcss/vite` plugin configured in `vite.config.ts`. `src/app/styles/index.css` imports Tailwind with `@import 'tailwindcss';`. Use Tailwind utility classes directly in JSX and put global CSS or Tailwind v4 theme customization in that file. This scaffold does not need a Tailwind config file or PostCSS config.

`src/main.jsx` imports `src/app/styles/index.css`, so global font wiring belongs there. Keep CSS `@import` statements first, then add any `@font-face` rules and font-family defaults.

## Code quality

- Use double quotes for strings containing apostrophes (`"We're here to help"`), or escape them in single-quoted strings. An unescaped apostrophe in a single-quoted string breaks the build.
- Ensure JSX tags are closed and braces are balanced.
- Export components as default exports.
