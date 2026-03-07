# BusNotification

Aplicativo Android (Jetpack Compose) para pesquisar trajetos de ônibus, configurar janelas de notificação e acompanhar itens notificados.

## Visão geral

O projeto usa:

- **Jetpack Compose** para interface.
- **Navigation Compose** para fluxo entre telas.
- **Hilt** para injeção de dependência.
- **Retrofit + Gson** para integração com a API de rotas do Google.
- **Firebase Firestore** para persistência de configurações de notificação.

## Funcionalidades atuais

- **Tela inicial** com lista de ônibus carregada do Firestore + Google Routes API.
- **Busca de linha/destino** com debounce para evitar chamadas excessivas.
- **Configuração de notificação** com janela de horário (início/fim).
- **Persistência da configuração** em `locations/{lineCode}` no Firestore.
- **Histórico de notificações** (atualmente com dados de exemplo no `ViewModel`).

## Fluxo principal

1. Usuário abre a Home.
2. Toca em **Adicionar**.
3. Pesquisa um destino na tela de busca.
4. Seleciona um resultado de trajeto.
5. Ajusta a janela de notificação.
6. Salva a configuração (dados vão para Firestore).

## Stack técnica

- **Kotlin 2.0.21**
- **Android Gradle Plugin 8.13.2**
- **Compile/Target SDK 35** (min SDK 30)
- **Material 3 + Compose BOM 2024.09.00**
- **Coroutines**
- **Firebase (BoM + Firestore + Analytics)**

## Pré-requisitos

- Android Studio recente (Koala ou superior, recomendado).
- JDK 11.
- Android SDK com API 35.
- Projeto Firebase configurado para o app.
- Chave da Google Routes API.

## Configuração local

### 1) Chave da API do Google

O app lê a chave nesta ordem:

1. `local.properties` (`GOOGLE_API_KEY`)
2. variável de ambiente `GOOGLE_API_KEY`
3. string vazia (fallback)

Exemplo em `local.properties`:

```properties
GOOGLE_API_KEY=AIza...
```

### 2) Firebase

Garanta que o arquivo `app/google-services.json` exista localmente e pertença ao projeto Firebase correto.

### 3) Dependências

No diretório raiz:

```bash
./gradlew dependencies
```

## Como executar

```bash
./gradlew assembleDebug
```

Depois, rode a aplicação no emulador/dispositivo pelo Android Studio.

## Testes

```bash
./gradlew test
```

## Estrutura resumida

- `app/src/main/java/com/will/busnotification/ui`: telas Compose.
- `app/src/main/java/com/will/busnotification/viewmodel`: estado e regras de apresentação.
- `app/src/main/java/com/will/busnotification/repository`: acesso a dados e integração de APIs.
- `app/src/main/java/com/will/busnotification/di`: módulos de injeção com Hilt.
- `app/src/main/java/com/will/busnotification/data`: DTOs, mapeadores e contratos de rede.

## Observações importantes

- O agendamento real de notificações ainda está como `TODO` (atualmente apenas loga a ação no `DefaultNotificationScheduleManager`).
- Parte dos dados de histórico/notificações ainda é mockada (`loadNotifiedBuses`).
- Existe documentação extra para distribuição de APK em `APK_DOWNLOAD.md`.

## Próximos passos sugeridos

- Integrar WorkManager para notificações recorrentes reais.
- Persistir histórico real de notificações recebidas.
- Melhorar origem dinâmica da localização no fluxo de busca.
- Adicionar testes instrumentados para fluxo completo de criação de notificação.
