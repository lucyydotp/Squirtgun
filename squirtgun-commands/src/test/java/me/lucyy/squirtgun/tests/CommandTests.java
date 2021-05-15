package me.lucyy.squirtgun.tests;

import me.lucyy.squirtgun.command.context.StringContext;
import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.command.node.NodeBuilder;
import me.lucyy.squirtgun.platform.PermissionHolder;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommandTests {

	@Test
	@DisplayName("Check simple built command nodes work")
	public void testSimpleBuiltNodes() {
		Component component = Component.text("hello");
		CommandNode<PermissionHolder> node = new NodeBuilder<>()
			.name("test")
			.executes(ctx -> component)
			.build();

		Component returned = node.execute(new StringContext<>(new TestFormatter(),
			x -> true, node, "test"));
		Assertions.assertEquals(returned, component);
	}
}
