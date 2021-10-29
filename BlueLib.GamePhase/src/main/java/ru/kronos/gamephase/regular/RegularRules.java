package ru.kronos.gamephase.regular;

public class RegularRules {

    private BlockRule blockRule;
    private ItemRule itemRule;
    private CommandRule commandRule;
    private GodRule godRule;
    private EffectRule effectRule;

    public void setRegularRule(RegularRule rule) {
        if (rule instanceof BlockRule) {
            blockRule = (BlockRule) rule;
        } else if (rule instanceof CommandRule) {
            commandRule = (CommandRule) rule;
        } else if (rule instanceof EffectRule) {
            effectRule = (EffectRule) rule;
        } else if (rule instanceof GodRule) {
            godRule = (GodRule) rule;
        } else if (rule instanceof ItemRule) {
            itemRule = (ItemRule) rule;
        }
    }

    public BlockRule getBlockRule() {
        return blockRule;
    }

    public CommandRule getCommandRule() {
        return commandRule;
    }

    public EffectRule getEffectRule() {
        return effectRule;
    }

    public GodRule getGodRule() {
        return godRule;
    }

    public ItemRule getItemRule() {
        return itemRule;
    }

    public boolean hasBlockRule() {
        return blockRule != null;
    }

    public boolean hasCommandRule() {
        return commandRule != null;
    }

    public boolean hasEffectRule() {
        return effectRule != null;
    }

    public boolean hasGodRule() {
        return godRule != null;
    }

    public boolean hasItemRule() {
        return itemRule != null;
    }


}
