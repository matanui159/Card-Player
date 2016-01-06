package gen;

import java.lang.reflect.Field;

import org.lwjgl.glfw.GLFW;

public class GenKeys {
	public static void main(String[] args) throws IllegalAccessException {
		System.out.println("\t/* GENERATED CODE BEGINS HERE */");
		for (Field field : GLFW.class.getDeclaredFields()) {
			if (field.getName().startsWith("GLFW_KEY_")) {
				System.out.println("\tpublic static final int " + field.getName().replace("GLFW_", "")
						+ " = " + field.getInt(null) + ";");
			}
		}
		System.out.println("\t/* GENERATED CODE ENDS HERE */");
	}
}