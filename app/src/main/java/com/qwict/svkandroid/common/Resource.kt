package com.qwict.svkandroid.common

/**
 * A sealed class representing different states of a resource.
 *
 * @param T The type of data contained in the resource.
 * @property data The actual data.
 * @property message A message associated with the resource, usually describing an error.
 */
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    /**
     * Represents the loading state of a resource.
     *
     * @param data The data associated with the loading state.
     */
    class Loading<T>(data: T? = null) : Resource<T>(data)

    /**
     * Represents the success state of a resource.
     *
     * @param data The data associated with the success state.
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     * Represents the error state of a resource.
     *
     * @param data The data associated with the error state.
     * @param message A message describing the error.
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}
