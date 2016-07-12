package net.sharksystem.sharknet.javafx.model;

import javafx.util.StringConverter;
import net.sharkfw.knowledgeBase.TXSemanticTag;

/**
 * @Author Yves Kaufmann
 * @since 11.07.2016
 */
public class StringConverters  {

	public static StringConverter<TXSemanticTag> forTXSemanticTag() {
		return new StringConverter<TXSemanticTag>() {
			@Override
			public String toString(TXSemanticTag tag) {
				return tag.getName();
			}

			@Override
			public TXSemanticTag fromString(String string) {
				throw new UnsupportedOperationException("conversion from string to tag isn't supported");
			}
		};
	};

}
