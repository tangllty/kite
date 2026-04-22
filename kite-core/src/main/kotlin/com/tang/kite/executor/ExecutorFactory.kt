package com.tang.kite.executor

import com.tang.kite.transaction.Transaction

/**
 * @author Tang
 */
interface ExecutorFactory {

    fun newExecutor(transaction: Transaction): Executor

}
