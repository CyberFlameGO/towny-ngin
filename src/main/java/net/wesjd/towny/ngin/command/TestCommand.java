package net.wesjd.towny.ngin.command;

import net.wesjd.towny.ngin.command.framework.Commandable;
import net.wesjd.towny.ngin.command.framework.annotation.Command;
import net.wesjd.towny.ngin.command.framework.annotation.SubCommand;

public class TestCommand implements Commandable {

    @Command(name = "hello", desc = "yeet")
    public void test() {
        System.out.println("asdfasdfasd IT WORKED 1");
    }

    @SubCommand(of = "hello", name = "yeet", desc = "yeet2")
    public void test2() {
        System.out.println("asdfasdfasd IT WORKED 2");
    }

}
