/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

import {defineConfig} from "vite";
import {resolve} from "path";
import reactRefresh from "@vitejs/plugin-react-refresh";
import postcssNesting from "postcss-nesting";

const src=resolve(process.env.src || "src/main/javascript/");
const out=resolve(process.env.out || "target/classes/static/");

export default defineConfig(({ mode }) => ({ // https://vitejs.dev/config/

    root: src,

    publicDir: "files",

    plugins: [reactRefresh()],

    css: {
        postcss: {
            plugins: [postcssNesting()]
        }
    },

    resolve: {
        alias: {
            "@ec2u/card": resolve(src)
        }
    },

    build: {

        outDir: out,
        assetsDir: ".",
        emptyOutDir: true,
        minify: mode !== "development",

        rollupOptions: {
            output: { manualChunks: undefined } // no vendor chunks
        }

    },

    server: {

        https: true,
        host: "127.0.0.1", // as required by GWS SAML configuration
        open: "/index.html", // as asset to avoid proxying

        proxy: {
            "^(/[-a-zA-Z0-9]+)*/?(\\?.*)?$": { // routes with optional query
                target: "http://localhost:8080/",
                xfwd: true // required to properly generate SAML URLs
            }
        }

    }

}));
