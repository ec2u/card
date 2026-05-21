/*
 * Copyright © 2020-2026 EC2U Alliance
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
import {readFileSync} from "fs";
import reactRefresh from "@vitejs/plugin-react-refresh";
import postcssNesting from "postcss-nesting";

const src=resolve("code/javascript/");
const out=resolve("code/static/");
const samples=resolve("code/samples/");

process.env.card_version=new Date().toISOString();


export default defineConfig(({ mode }) => ({ // https://vitejs.dev/config/

    root: src,

    publicDir: "files",
    envPrefix: "card_",

    plugins: [reactRefresh(), mockBackend()],

    css: {
        postcss: {
            plugins: [postcssNesting()]
        }
    },

    resolve: {
        alias: [
            { find: /^@ec2u\/card\/(.*)$/, replacement: resolve(src, "$1") }
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

        host: "127.0.0.1",
        port: 3000,
        strictPort: true

    }

}));


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

function mockBackend() {

    return {

        name: "card-mock-backend",

        configureServer(server) {

            const SessionCookie="card-dev-session";


            server.middlewares.use("/v1", (req, res) => {
                if ( hasSession(req) ) {
                    res.setHeader("Content-Type", "application/json");
                    res.end(readFileSync(resolve(samples, "profile.json"), "utf8"));
                } else {
                    res.statusCode=401;
                    res.end();
                }
            });

            server.middlewares.use("/Shibboleth.sso/Login", (req, res) => {
                redirect(res, queryParam(req, "target") ?? "/", `${SessionCookie}=1; Path=/`);
            });

            server.middlewares.use("/Shibboleth.sso/Logout", (req, res) => {
                redirect(res, queryParam(req, "return") ?? "/", `${SessionCookie}=; Path=/; Max-Age=0`);
            });


            function hasSession(req) {
                return (req.headers.cookie ?? "").split(";").map(s => s.trim()).includes(`${SessionCookie}=1`);
            }

            function queryParam(req, name) {
                return new URL(req.url, "http://localhost").searchParams.get(name);
            }

            function redirect(res, location, cookie) {
                res.statusCode=302;
                res.setHeader("Location", location);
                if ( cookie ) { res.setHeader("Set-Cookie", cookie); }
                res.end();
            }

        }

    };

}
