# Download do APK via Internet

Este repositório possui uma GitHub Action para gerar APK e publicar em caminho fixo no branch.

## Quando roda

- `push` na `main`
- `pull_request` para `main`
- execução manual (`workflow_dispatch`)

## Pré-requisito (obrigatório)

Como o projeto usa Firebase/Google Services, configure antes o secret do repositório:

- Nome do secret: `GOOGLE_SERVICES_JSON`
- Valor: conteúdo **completo** do seu `app/google-services.json`

Caminho no GitHub: **Settings → Secrets and variables → Actions → New repository secret**.

## Como baixar

### Push na main (link público/raw)

Depois da execução em `push` na `main`, o APK é commitado em `app/release/app-release.apk`.

URL:

`https://raw.githubusercontent.com/<OWNER>/<REPO>/refs/heads/main/app/release/app-release.apk`

### Pull Request para main (artifact da Action)

Em `pull_request`, o workflow gera e publica o APK como artifact (`app-release`) no run da Action.

> Observação: em PR não é feito commit automático para evitar problemas de permissão do `GITHUB_TOKEN`.
