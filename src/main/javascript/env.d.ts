interface ImportMeta {
	readonly env: ImportMetaEnv;
}

interface ImportMetaEnv extends Readonly<Record<string, boolean | string>> {

	readonly MODE: string;
	readonly BASE_URL: string;
	readonly PROD: boolean;
	readonly DEV: boolean;

}
