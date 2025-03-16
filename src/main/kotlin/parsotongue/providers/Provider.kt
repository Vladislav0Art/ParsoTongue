package parsotongue.providers

interface Provider<in Parameter, out Entity> {
    fun get(parameter: Parameter): Entity
}