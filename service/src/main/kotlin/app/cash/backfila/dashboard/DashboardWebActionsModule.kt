package app.cash.backfila.dashboard

import misk.environment.Environment
import misk.inject.KAbstractModule
import misk.web.DashboardTab
import misk.web.WebActionModule
import misk.web.actions.AdminDashboardTab
import misk.web.metadata.WebTabResourceModule

class DashboardWebActionsModule(val environment: Environment) : KAbstractModule() {
  override fun configure() {
    install(WebActionModule.create<GetServicesAction>())
    install(WebActionModule.create<CreateBackfillAction>())
    install(WebActionModule.create<StartBackfillAction>())
    install(WebActionModule.create<StopBackfillAction>())
    install(WebActionModule.create<GetRegisteredBackfillsAction>())
    install(WebActionModule.create<GetBackfillRunsAction>())
    install(WebActionModule.create<GetBackfillStatusAction>())
    install(WebActionModule.create<UpdateBackfillAction>())
    install(WebActionModule.create<RootRedirectAction>())

    // Tabs
    multibind<DashboardTab, AdminDashboardTab>().toInstance(DashboardTab(
        name = "App",
        slug = "app",
        url_path_prefix = "/app/",
        category = "Backfila"
    ))
    install(WebTabResourceModule(
        environment = environment,
        slug = "app",
        web_proxy_url = "http://localhost:4200/",
        url_path_prefix = "/app/",
        resourcePath = "classpath:/web/app/"
    ))
  }
}