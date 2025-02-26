package com.invoicegenerator.modeles;

/**
 * Represents the result of an action with a success status and a message.
 */
public record ActionResult(boolean success, String message) {

    /**
     * Combines this ActionResult with another one.
     *
     * @param term The other ActionResult to combine with.
     * @return A new ActionResult with combined success status and messages.
     */
    public ActionResult plus(ActionResult term) {
        boolean combinedSuccess = this.success && term.success();
        String combinedMessage = this.message + " " + term.message();
        return new ActionResult(combinedSuccess, combinedMessage);
    }

    /**
     * Returns a string representation of the ActionResult.
     *
     * @return A string representation of the ActionResult.
     */
    @Override
    public String toString() {
        return "ActionResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}