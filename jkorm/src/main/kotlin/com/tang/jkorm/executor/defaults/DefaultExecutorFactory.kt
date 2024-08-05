package com.tang.jkorm.executor.defaults

import com.tang.jkorm.executor.Executor
import com.tang.jkorm.executor.ExecutorFactory
import com.tang.jkorm.session.Configuration
import com.tang.jkorm.transaction.Transaction

class DefaultExecutorFactory : ExecutorFactory {

    override fun newExecutor(configuration: Configuration, transaction: Transaction): Executor {
        return DefaultExecutor(configuration, transaction)
    }

}
