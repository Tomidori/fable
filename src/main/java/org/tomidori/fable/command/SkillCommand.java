package org.tomidori.fable.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.registry.FableRegistries;
import org.tomidori.fable.skill.Skill;
import org.tomidori.fable.skill.SkillResponse;

import java.util.Collection;
import java.util.List;

public final class SkillCommand {
    private static final DynamicCommandExceptionType UNKNOWN_SKILL_EXCEPTION = new DynamicCommandExceptionType(id ->
            Text.stringifiedTranslatable("skill.notFound", id)
    );
    private static final DynamicCommandExceptionType FAILED_ENTITY_EXCEPTION = new DynamicCommandExceptionType(entityName ->
            Text.translatable("commands.skill.failed.entity", entityName)
    );
    private static final Dynamic2CommandExceptionType ADD_FAILURE_EXCEPTION = new Dynamic2CommandExceptionType((entityName, skillName) ->
            Text.translatable("commands.skill.add.failure", entityName, skillName)
    );
    private static final Dynamic2CommandExceptionType REMOVE_FAILURE_EXCEPTION = new Dynamic2CommandExceptionType((entityName, skillName) ->
            Text.translatable("commands.skill.remove.failure", entityName, skillName)
    );
    private static final Dynamic3CommandExceptionType TEST_FAILURE_EXCEPTION = new Dynamic3CommandExceptionType((entityName, skillName, reason) ->
            Text.translatable("commands.skill.test.failure", entityName, skillName, reason)
    );
    private static final Dynamic3CommandExceptionType CAST_FAILURE_EXCEPTION = new Dynamic3CommandExceptionType((entityName, skillName, reason) ->
            Text.translatable("commands.skill.cast.failure", entityName, skillName, reason)
    );
    private static final DynamicCommandExceptionType CANCEL_FAILURE_EXCEPTION = new DynamicCommandExceptionType(entityName ->
            Text.translatable("commands.skill.cancel.failure", entityName)
    );
    private static final DynamicCommandExceptionType INTERRUPT_FAILURE_EXCEPTION = new DynamicCommandExceptionType(entityName ->
            Text.translatable("commands.skill.interrupt.failure", entityName)
    );
    private static final DynamicCommandExceptionType TERMINATE_FAILURE_EXCEPTION = new DynamicCommandExceptionType(entityName ->
            Text.translatable("commands.skill.terminate.failure", entityName)
    );

    private SkillCommand() {
    }

    @ApiStatus.Internal
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(argumentSkill());
    }

    private static LiteralArgumentBuilder<ServerCommandSource> argumentSkill() {
        return CommandManager.literal("skill")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argumentAdd())
                .then(argumentRemove())
                .then(argumentTest())
                .then(argumentCast())
                .then(argumentCancel())
                .then(argumentInterrupt())
                .then(argumentTerminate());
    }

    private static LiteralArgumentBuilder<ServerCommandSource> argumentAdd() {
        return CommandManager.literal("add")
                .then(
                        CommandManager.argument("skill", IdentifierArgumentType.identifier())
                                .suggests((context, builder) ->
                                        CommandSource.suggestIdentifiers(
                                                FableRegistries.SKILL.getIds(),
                                                builder
                                        )
                                )
                                .executes(context ->
                                        executeAdd(
                                                context.getSource(),
                                                getSkill(IdentifierArgumentType.getIdentifier(context, "skill")),
                                                List.of(context.getSource().getEntityOrThrow())
                                        )
                                )
                                .then(
                                        CommandManager.argument("targets", EntityArgumentType.entities())
                                                .executes(context ->
                                                        executeAdd(
                                                                context.getSource(),
                                                                getSkill(IdentifierArgumentType.getIdentifier(context, "skill")),
                                                                EntityArgumentType.getEntities(context, "targets")
                                                        )
                                                )
                                )
                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> argumentRemove() {
        return CommandManager.literal("remove")
                .then(
                        CommandManager.argument("skill", IdentifierArgumentType.identifier())
                                .suggests((context, builder) ->
                                        CommandSource.suggestIdentifiers(
                                                FableRegistries.SKILL.getIds(),
                                                builder
                                        )
                                )
                                .executes(context ->
                                        executeRemove(
                                                context.getSource(),
                                                getSkill(IdentifierArgumentType.getIdentifier(context, "skill")),
                                                List.of(context.getSource().getEntityOrThrow())
                                        )
                                )
                                .then(
                                        CommandManager.argument("targets", EntityArgumentType.entities())
                                                .executes(context ->
                                                        executeRemove(
                                                                context.getSource(),
                                                                getSkill(IdentifierArgumentType.getIdentifier(context, "skill")),
                                                                EntityArgumentType.getEntities(context, "targets")
                                                        )
                                                )
                                )
                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> argumentTest() {
        return CommandManager.literal("test")
                .then(
                        CommandManager.argument("skill", IdentifierArgumentType.identifier())
                                .suggests((context, builder) ->
                                        CommandSource.suggestIdentifiers(
                                                FableRegistries.SKILL.getIds(),
                                                builder
                                        )
                                )
                                .executes(context ->
                                        executeTest(
                                                context.getSource(),
                                                getSkill(IdentifierArgumentType.getIdentifier(context, "skill")),
                                                List.of(context.getSource().getEntityOrThrow())
                                        )
                                )
                                .then(
                                        CommandManager.argument("targets", EntityArgumentType.entities())
                                                .executes(context ->
                                                        executeTest(
                                                                context.getSource(),
                                                                getSkill(IdentifierArgumentType.getIdentifier(context, "skill")),
                                                                EntityArgumentType.getEntities(context, "targets")
                                                        )
                                                )
                                )
                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> argumentCast() {
        return CommandManager.literal("cast")
                .then(
                        CommandManager.argument("skill", IdentifierArgumentType.identifier())
                                .suggests((context, builder) ->
                                        CommandSource.suggestIdentifiers(
                                                FableRegistries.SKILL.getIds(),
                                                builder
                                        )
                                )
                                .executes(context ->
                                        executeCast(
                                                context.getSource(),
                                                getSkill(IdentifierArgumentType.getIdentifier(context, "skill")),
                                                List.of(context.getSource().getEntityOrThrow())
                                        )
                                )
                                .then(
                                        CommandManager.argument("targets", EntityArgumentType.entities())
                                                .executes(context ->
                                                        executeCast(
                                                                context.getSource(),
                                                                getSkill(IdentifierArgumentType.getIdentifier(context, "skill")),
                                                                EntityArgumentType.getEntities(context, "targets")
                                                        )
                                                )
                                )
                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> argumentCancel() {
        return CommandManager.literal("cancel")
                .executes(context ->
                        executeCancel(
                                context.getSource(),
                                List.of(context.getSource().getEntityOrThrow())
                        )
                )
                .then(
                        CommandManager.argument("targets", EntityArgumentType.entities())
                                .executes(context ->
                                        executeCancel(
                                                context.getSource(),
                                                EntityArgumentType.getEntities(context, "targets")
                                        )
                                )
                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> argumentInterrupt() {
        return CommandManager.literal("interrupt")
                .executes(context ->
                        executeInterrupt(
                                context.getSource(),
                                List.of(context.getSource().getEntityOrThrow())
                        )
                )
                .then(
                        CommandManager.argument("targets", EntityArgumentType.entities())
                                .executes(context ->
                                        executeInterrupt(
                                                context.getSource(),
                                                EntityArgumentType.getEntities(context, "targets")
                                        )
                                )
                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> argumentTerminate() {
        return CommandManager.literal("terminate")
                .executes(context ->
                        executeTerminate(
                                context.getSource(),
                                List.of(context.getSource().getEntityOrThrow())
                        )
                )
                .then(
                        CommandManager.argument("targets", EntityArgumentType.entities())
                                .executes(context ->
                                        executeTerminate(
                                                context.getSource(),
                                                EntityArgumentType.getEntities(context, "targets")
                                        )
                                )
                );
    }

    private static int executeAdd(
            ServerCommandSource source,
            RegistryEntry<? extends Skill<?>> skill,
            Collection<? extends Entity> targets
    ) throws CommandSyntaxException {
        for (Entity target : targets) {
            if (!getLivingEntity(target).getSkillContainer().addSkill(skill)) {
                throw ADD_FAILURE_EXCEPTION.create(target.getName(), skill.value().getName());
            }
        }

        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.skill.add.success.single", targets.iterator().next().getName(), skill.value().getName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.skill.add.success.multiple", targets.size(), skill.value().getName()), true);
        }

        return targets.size();
    }

    private static int executeRemove(
            ServerCommandSource source,
            RegistryEntry<? extends Skill<?>> skill,
            Collection<? extends Entity> targets
    ) throws CommandSyntaxException {
        for (Entity target : targets) {
            if (!getLivingEntity(target).getSkillContainer().removeSkill(skill)) {
                throw REMOVE_FAILURE_EXCEPTION.create(target.getName(), skill.value().getName());
            }
        }

        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.skill.remove.success.single", targets.iterator().next().getName(), skill.value().getName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.skill.remove.success.multiple", targets.size(), skill.value().getName()), true);
        }

        return targets.size();
    }

    private static int executeTest(
            ServerCommandSource source,
            RegistryEntry<? extends Skill<?>> skill,
            Collection<? extends Entity> targets
    ) throws CommandSyntaxException {
        for (Entity target : targets) {
            if (getLivingEntity(target).getSkillManager().canCastSkill(skill) instanceof SkillResponse.Failure failure) {
                throw TEST_FAILURE_EXCEPTION.create(target.getName(), skill.value().getName(), failure.getReason());
            }
        }

        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.skill.test.success.single", targets.iterator().next().getName(), skill.value().getName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.skill.test.success.multiple", targets.size(), skill.value().getName()), true);
        }

        return targets.size();
    }

    private static int executeCast(
            ServerCommandSource source,
            RegistryEntry<? extends Skill<?>> skill,
            Collection<? extends Entity> targets
    ) throws CommandSyntaxException {
        for (Entity target : targets) {
            if (getLivingEntity(target).getSkillManager().castSkill(skill) instanceof SkillResponse.Failure failure) {
                throw CAST_FAILURE_EXCEPTION.create(target.getName(), skill.value().getName(), failure.getReason());
            }
        }

        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.skill.cast.success.single", targets.iterator().next().getName(), skill.value().getName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.skill.cast.success.multiple", targets.size(), skill.value().getName()), true);
        }

        return targets.size();
    }

    private static int executeCancel(
            ServerCommandSource source,
            Collection<? extends Entity> targets
    ) throws CommandSyntaxException {
        for (Entity target : targets) {
            if (!getLivingEntity(target).getSkillManager().cancelCastingSkill()) {
                throw CANCEL_FAILURE_EXCEPTION.create(target.getName());
            }
        }

        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.skill.cancel.success.single", targets.iterator().next().getName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.skill.cancel.success.multiple", targets.size()), true);
        }

        return targets.size();
    }

    private static int executeInterrupt(
            ServerCommandSource source,
            Collection<? extends Entity> targets
    ) throws CommandSyntaxException {
        for (Entity target : targets) {
            if (!getLivingEntity(target).getSkillManager().interruptCastingSkill()) {
                throw INTERRUPT_FAILURE_EXCEPTION.create(target.getName());
            }
        }

        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.skill.interrupt.success.single", targets.iterator().next().getName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.skill.interrupt.success.multiple", targets.size()), true);
        }

        return targets.size();
    }

    private static int executeTerminate(
            ServerCommandSource source,
            Collection<? extends Entity> targets
    ) throws CommandSyntaxException {
        for (Entity target : targets) {
            if (!getLivingEntity(target).getSkillManager().terminateCastingSkill()) {
                throw TERMINATE_FAILURE_EXCEPTION.create(target.getName());
            }
        }

        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.skill.terminate.success.single", targets.iterator().next().getName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.skill.terminate.success.multiple", targets.size()), true);
        }

        return targets.size();
    }

    private static LivingEntity getLivingEntity(Entity entity) throws CommandSyntaxException {
        if (entity instanceof LivingEntity livingEntity) {
            return livingEntity;
        } else {
            throw FAILED_ENTITY_EXCEPTION.create(entity.getName());
        }
    }

    private static RegistryEntry<? extends Skill<?>> getSkill(Identifier id) throws CommandSyntaxException {
        return FableRegistries.SKILL.getEntry(id)
                .orElseThrow(() -> UNKNOWN_SKILL_EXCEPTION.create(id));
    }
}
