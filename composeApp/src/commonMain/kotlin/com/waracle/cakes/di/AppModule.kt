package com.waracle.cakes.di

import com.waracle.cakes.data.remote.CakeApi
import com.waracle.cakes.data.remote.createHttpClient
import com.waracle.cakes.data.repository.CakeRepositoryImpl
import com.waracle.cakes.domain.repository.CakeRepository
import com.waracle.cakes.domain.usecase.GetCakesUseCase
import com.waracle.cakes.presentation.cakelist.CakeListViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { createHttpClient(get()) }
    singleOf(::CakeApi)
    singleOf(::CakeRepositoryImpl) bind CakeRepository::class
    factoryOf(::GetCakesUseCase)
    viewModelOf(::CakeListViewModel)
}

// Called once from each platform's entry point.
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule, platformModule)
    }
}
