package data.infra.di

import data.infra.IdolRepository
import org.koin.dsl.module

object Repositories {
    val Module get() = module {
        single { IdolRepository(get()) }
    }
}
