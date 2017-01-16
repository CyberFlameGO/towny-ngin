package net.wesjd.towny.ngin.command.provider;

import com.sk89q.intake.parametric.Provider;

import java.util.Collections;
import java.util.List;

abstract class BaseProvider<T> implements Provider<T> {

    @Override
    public boolean isProvided() {
        return true;
    }

    @Override
    public List<String> getSuggestions(String s) {
        return Collections.emptyList();
    }

}
