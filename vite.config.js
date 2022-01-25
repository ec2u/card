/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

import {defineConfig} from "vite";
import {resolve} from "path";
import reactRefresh from "@vitejs/plugin-react-refresh";
import postcssNesting from "postcss-nesting";

const src=resolve(process.env.src || "src/main/javascript/");
const out=resolve(process.env.out || "target/static/");

export default defineConfig(({ mode }) => ({ // https://vitejs.dev/config/

	root: src,

	publicDir: "files",

	plugins: [reactRefresh()],

	css: {
		postcss: {
			plugins: [postcssNesting()]
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
		port: 3001,
		strictPort: true
	}

}));
