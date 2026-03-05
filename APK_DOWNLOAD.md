# Download do APK via Internet

Este repositório possui uma GitHub Action para gerar APK e publicar em caminho fixo no branch.

## Pré-requisito (obrigatório)

Como o projeto usa Firebase/Google Services, configure antes o secret do repositório:

- Nome do secret: `GOOGLE_SERVICES_JSON`
- Valor: conteúdo **completo** do seu `app/google-services.json`

Caminho no GitHub: **Settings → Secrets and variables → Actions → New repository secret**.

## Como gerar

1. Vá em **Actions** no GitHub.
2. Execute o workflow **Build and publish APK** manualmente.
3. Ao final, ele salva o arquivo em `app/release/app-release.apk` no branch atual.

## URL de download

Depois da execução, você poderá baixar em:

`https://raw.githubusercontent.com/<OWNER>/<REPO>/<BRANCH>/app/release/app-release.apk`

Exemplo de formato:

`https://raw.githubusercontent.com/Luminary-Team/eden-mobile/refs/heads/main/app/release/app-release.apk`

> Observação: o workflow gera a variante **debug** (`assembleDebug`) para simplificar a instalação sem assinatura release.
