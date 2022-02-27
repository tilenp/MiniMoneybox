## Architecture:
 - MVVM
 - view - viewModel - repository - cache / service - network
 - concrete classes depend on interfaces

## Libraries:
 - Dagger
 - MockK
 - MockWebServer
 - Navigation Component
 - Retrofit
 - RxJava

## Storage & data consistency:
 - views observe local cache through repositories.

## Testing: 
 - I unit tested the main viewModels, repositories and services. Mappers are tested indirectly.

Good luck!
