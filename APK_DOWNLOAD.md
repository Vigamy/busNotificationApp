# Download do APK via Internet

Este repositório agora possui uma GitHub Action para gerar um APK e publicar em um caminho fixo no branch.

## Como gerar

1. Vá em **Actions** no GitHub.
2. Execute o workflow **Build and publish APK** manualmente.
3. Ao final, ele salva o arquivo em `app/release/app-release.apk` no branch atual.

## URL de download

Depois da execução, você poderá baixar em:

`https://raw.githubusercontent.com/<OWNER>/<REPO>/<BRANCH>/app/release/app-release.apk`

Exemplo de formato:

`https://raw.githubusercontent.com/Luminary-Team/eden-mobile/refs/heads/main/app/release/app-release.apk`

> Observação: o workflow gera a variante **debug** (`assembleDebug`) para garantir instalação fácil sem configuração de assinatura de release.
