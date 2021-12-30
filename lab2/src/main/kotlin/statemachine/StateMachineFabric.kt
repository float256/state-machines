package statemachine

import java.io.InputStream

interface StateMachineFabric<T: StateMachine<out Any>> {
    fun create(inputStream: InputStream): T
    fun create(stateMachine: StateMachine<out Any>): T
}