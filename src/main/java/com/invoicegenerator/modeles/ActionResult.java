package com.invoicegenerator.modeles;

public record ActionResult(boolean success, String message) {

    public ActionResult plus(ActionResult term) {
        boolean combinedSuccess = this.success && term.success();
        String combinedMessage = this.message + " " + term.message();
        return new ActionResult(combinedSuccess, combinedMessage);
    }

    @Override
    public String toString() {
        return "ActionResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}