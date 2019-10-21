package io.brachu.docker.compose.plugin;

import io.brachu.johann.exception.JohannTimeoutException;

interface ExceptionMessages {

    static String clusterTimeout(JohannTimeoutException ex) {
        return "Timed out while waiting for cluster to be healthy."
                + " Either at least one of containers in the cluster failed to start properly or the value of 'wait' property is too short."
                + " Current 'wait' property value is " + ex.getTime() + " " + ex.getUnit() + ".";
    }

    static String serviceTimeout(String serviceName, JohannTimeoutException ex) {
        return "Timed out while waiting for '" + serviceName + "' service to be healthy."
                + " Either at least one of service's containers failed to start properly or the value of 'wait' property is too short."
                + " Current 'wait' property value is " + ex.getTime() + " " + ex.getUnit() + ".";
    }

}
