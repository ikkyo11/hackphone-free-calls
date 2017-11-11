package app.freecalls.config;

import hackphone.phone.PhoneDriver;

@FunctionalInterface
public interface SecondLegDriver {

    PhoneDriver secondLegDriver();
}
