/*
 * Copyright © 2020-2022 EC2U Alliance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
        alias: [
            { find: /^@ec2u\/card\/(.*)$/, replacement: resolve(src, "$1") },
        ]
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
        open: "/",

        proxy: {
            "^(/[-a-zA-Z0-9]+)*/?(\\?.*)?$": { // routes with optional query
                target: "http://localhost:8080/",
                xfwd: true // required to properly generate SAML URLs
            }
        }

    }

}));
