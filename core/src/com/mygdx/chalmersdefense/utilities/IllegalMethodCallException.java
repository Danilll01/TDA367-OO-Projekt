package com.mygdx.chalmersdefense.utilities;

/**
 * @author Joel Båtsman Hilmersson
 * <p>
 * Exeption to call if a method should not be called
 */
final class IllegalMethodCallException extends RuntimeException {
    public IllegalMethodCallException(String message) {
        super(message);
    }
}
