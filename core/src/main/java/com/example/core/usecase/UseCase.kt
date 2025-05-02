package com.example.core.usecase

import com.example.core.common.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


abstract class UseCase<in Params, out Type>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    protected abstract suspend fun execute(params: Params): Result<Type>

    suspend operator fun invoke(params: Params): Result<Type> {
        return try {
            withContext(dispatcher) {
                execute(params)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}


abstract class NoParamUseCase<out Type>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    protected abstract suspend fun execute(): Result<Type>

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


abstract class FlowUseCase<in Params, out Type>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    abstract fun execute(params: Params): kotlinx.coroutines.flow.Flow<Result<Type>>

    operator fun invoke(params: Params): kotlinx.coroutines.flow.Flow<Result<Type>> {
        return execute(params)
    }
}