/// <reference types="vite/client" />
interface ImportMetaEnv {
  readonly VITE_SERVER_URL: string // URL del backend
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
