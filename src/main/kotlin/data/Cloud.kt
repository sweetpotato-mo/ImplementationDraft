package data

class Cloud(
    val id: Int,
    duration: Int,
    amount: Int,
    location: Int
) {
    companion object{
        const val MAX_MOVEMENT_QUOTA = 10
        const val RAIN_TRIGGER_AMOUNT = 5000
    }

    // Private setters, but public getters.
    var duration: Int = duration
        private set

    var amount: Int = amount
        private set

    var location: Int = location
        private set

    /**
     * The maximum number of tiles a cloud can move in a single tick.
     * The specification states this is always a maximum of 10.
     * This is an internal property used to track movement for the current tick.
     */
    var movementQuota: Int = MAX_MOVEMENT_QUOTA
        private set


    /** A secondary constructor to allow passing in a movementQuota.
     * This constructor delegates to the primary constructor.
     */
    constructor(id: Int, duration: Int, amount: Int, location: Int, movementQuota: Int) :
            this(id, duration, amount, location) {
        // This is where you enforce the rule that movementQuota cannot be more than 10.
        this.movementQuota = minOf(movementQuota, MAX_MOVEMENT_QUOTA)
    }


    /**
     * Reduces the total water amount of the cloud.
     * This method is used when the cloud rains on a tile or when a machine is irrigating.
     *
     * @param amt The amount of water to be decreased.
     */
    fun decreaseCloudAmount(amt: Int) {
        amount -= amt
    }

    /**
     * Decrements the remaining movement quota for the current tick.
     * This method is called after the cloud moves to an adjoining tile.
     */
    fun decrementMovementQuota() {
        movementQuota--.coerceAtLeast(0)
    }

    /**
     * Determines if the cloud should dissipate based on its water amount.
     * The cloud dissipates if its water amount drops to 0 or less.
     *
     * There are three ways a cloud can dissipate, which are handled in different parts of the code:
     * 1. Due to duration: The `EnvironmentController` will decrement the duration of all clouds at
     *  the end of a tick in `finalCloudBehaviour()`and remove those whose duration has reached 0.
     * 2. By moving onto a village tile: The cloud will be removed immediately upon entering a village
     *  tile, which is a logic handled within the cloud movement loop.
     * 3. When its water amount becomes empty: This is the condition checked by this function, which will
     *  be called after a rain event.
     *
     * @return True if the cloud's water amount is less than or equal to 0, otherwise false.
     */
    fun shouldDissipate(): Boolean {
        // This function checks only one of the three dissipation conditions:
        // when the cloud's water amount becomes empty.
        return amount <= 0
    }

    fun setLocation(location: Int){
        this.location = location
    }

    fun decrementDuration(){
        duration--.coerceAtLeast(0)
    }

    fun setMovementQuota(quota: Int){
        movementQuota = quota
    }


}