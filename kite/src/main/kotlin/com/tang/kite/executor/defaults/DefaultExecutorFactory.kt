package com.tang.kite.executor.defaults

import com.tang.kite.executor.Executor
import com.tang.kite.executor.ExecutorFactory
import com.tang.kite.session.Configuration
import com.tang.kite.transaction.Transaction

/**
 * @author Tang
 */
class DefaultExecutorFactory : ExecutorFactory {

    override fun newExecutor(configuration: Configuration, transaction: Transaction): Executor {
        return DefaultExecutor(configuration, transaction)
    }

}
