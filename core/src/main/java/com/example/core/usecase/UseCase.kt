package com.example.core.usecase

import com.example.core.common.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Базовый класс для всех Use Cases, принимающих параметры
 */
abstract class UseCase<in Params, out Type>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    // Абстрактный метод, который нужно реализовать в наследниках
    protected abstract suspend fun execute(params: Params): Result<Type>

    // Позволяет вызывать UseCase как функцию: useCase(params)
    suspend operator fun invoke(params: Params): Result<Type> {
        return try {
            // Переключаемся на IO-диспетчер для выполнения операции
            withContext(dispatcher) {
                execute(params)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

/**
 * Базовый класс для всех Use Cases без параметров
 */
abstract class NoParamUseCase<out Type>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    // Абстрактный метод для реализации
    protected abstract suspend fun execute(): Result<Type>

    // Позволяет вызывать UseCase как функцию: useCase()
    suspend operator fun invoke(): Result<Type> {
        return try {
            withContext(dispatcher) {
                execute()
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

/**
 * Базовый класс для Use Cases возвращающих Flow
 */
abstract class FlowUseCase<in Params, out Type>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    // Абстрактный метод, возвращающий Flow
    abstract fun execute(params: Params): kotlinx.coroutines.flow.Flow<Result<Type>>

    // Оператор вызова, возвращающий Flow
    operator fun invoke(params: Params): kotlinx.coroutines.flow.Flow<Result<Type>> {
        return execute(params)
    }
}