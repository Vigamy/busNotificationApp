# Download do APK via GitHub Releases

Este repositório possui uma GitHub Action para gerar o APK automaticamente e publicar na **release `latest`**.

## Como funciona

- O workflow `Build and publish APK release` roda automaticamente em cada push na `main`.
- Também pode ser executado manualmente pelo botão **Run workflow** em **Actions**.
- Ao final, ele atualiza a release `latest` com o arquivo `app-release.apk`.

## URL fixa de download

Use sempre esta URL (sem trocar owner/repo):

`https://github.com/<OWNER>/<REPO>/releases/download/latest/app-release.apk`

Exemplo:

`https://github.com/Luminary-Team/eden-mobile/releases/download/latest/app-release.apk`

## Pré-requisito obrigatório

Se o projeto usa Firebase e o arquivo `app/google-services.json` não está versionado, configure o secret:

- Nome: `GOOGLE_SERVICES_JSON`
- Valor: conteúdo **base64** do arquivo `app/google-services.json`

Gerar base64 localmente:

```bash
base64 -w 0 app/google-services.json
```

> Observação: o workflow gera a variante **debug** (`assembleDebug`) para não depender de assinatura de release.
