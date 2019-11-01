package app.cash.backfila.service

import app.cash.backfila.api.ServiceWebActionsModule
import app.cash.backfila.client.BackfilaClientServiceClientProvider
import app.cash.backfila.client.Connectors
import app.cash.backfila.client.EnvoyClientServiceClientProvider
import app.cash.backfila.client.ForConnectors
import app.cash.backfila.dashboard.DashboardWebActionsModule
import com.google.common.util.concurrent.ListeningExecutorService
import com.google.common.util.concurrent.MoreExecutors
import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.google.inject.Provides
import misk.config.ConfigModule
import misk.environment.Environment
import misk.environment.EnvironmentModule
import misk.inject.KAbstractModule
import misk.security.authz.AccessAnnotationEntry
import misk.slack.SlackModule
import misk.web.metadata.AdminDashboardAccess
import java.util.concurrent.Executors
import javax.inject.Singleton

class BackfilaServiceModule(
  private val environment: Environment,
  private val config: BackfilaConfig
) : KAbstractModule() {
  override fun configure() {
    multibind<AccessAnnotationEntry>().toInstance(
        AccessAnnotationEntry<AdminDashboardAccess>(capabilities = listOf("backfila--owners")))

    install(ConfigModule.create("backfila", config))
    install(EnvironmentModule(environment))
    install(BackfilaPersistenceModule(config))
    install(DashboardWebActionsModule(environment))
    install(ServiceWebActionsModule())

    install(SchedulerLifecycleServiceModule())

    newMapBinder<String, BackfilaClientServiceClientProvider>(ForConnectors::class)
        .addBinding(Connectors.ENVOY)
        .to(EnvoyClientServiceClientProvider::class.java)
    // TODO http connector

    if (config.slack != null) {
      install(SlackModule(config.slack))
    }
  }

  @Provides @ForBackfilaScheduler @Singleton
  fun backfillRunnerExecutor(): ListeningExecutorService {
    return MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(40,
        ThreadFactoryBuilder()
            .setNameFormat("backfila-runner-%d")
            .build()))
  }
}
