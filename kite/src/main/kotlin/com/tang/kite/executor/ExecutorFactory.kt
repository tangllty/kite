package com.tang.kite.executor

import com.tang.kite.session.Configuration
import com.tang.kite.transaction.Transaction

/**
 * @author Tang
 */
interface ExecutorFactory {

    fun newExecutor(configuration: Configuration, transaction: Transaction): Executor

}
