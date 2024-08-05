package com.tang.jkorm.executor

import com.tang.jkorm.session.Configuration
import com.tang.jkorm.transaction.Transaction

/**
 * @author Tang
 */
interface ExecutorFactory {

    fun newExecutor(configuration: Configuration, transaction: Transaction): Executor

}
