package com.invoicegenerator.modeles;

/**
 * Représente le résultat d'une action avec un statut de succès et un message.
 */
public record ActionResult(boolean success, String message) {

    /**
     * Combine cet ActionResult avec un autre.
     *
     * @param term L'autre ActionResult à combiner avec celui-ci.
     * @return Un nouvel ActionResult avec un statut de succès combiné et les messages combinés.
     */
    public ActionResult plus(ActionResult term) {
        boolean combinedSuccess = this.success && term.success();
        String combinedMessage = this.message + " " + term.message();
        return new ActionResult(combinedSuccess, combinedMessage);
    }

    /**
     * Retourne une représentation sous forme de chaîne de l'ActionResult.
     *
     * @return Une représentation sous forme de chaîne de l'ActionResult.
     */
    @Override
    public String toString() {
        return "ActionResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}