package entity;

import java.util.ArrayList;
import java.util.List;

public class KeywordNode {

	String keyword;
	List<KeywordNode> subsequentKeywords;

	/**
	 * This is constructor for KeywordNode.
	 * This Node serve as an entity to form a Linked List.
	 */
	//@author A0111916M
	public KeywordNode() {
		this(new String(), new ArrayList<KeywordNode>());

	}

	/**
	 * This is constructor for KeywordNode .
	 *
	 * @param String
	 *            The keyword that need to be created
	 * @return
	 */

	public KeywordNode(String keyword) {
		this(keyword, new ArrayList<KeywordNode>());

	}

	/**
	 * This is constructor for KeywordNode .
	 *
	 * @param String
	 *            The keyword that need to be created
	 * @param List
	 *            The List of keyword Node that need to be put in.
	 * @return
	 */

	private KeywordNode(String keyword, List<KeywordNode> subsequentKeywords) {
		this.setKeyword(keyword);
		this.setSubsequentKeywords(subsequentKeywords);
	}

	/**
	 * This is the method to get keyword from the KeywordNode.
	 *
	 * @return String return the keyword from the KeywordNode.
	 */

	public String getKeyword() {
		return keyword;
	}

	/**
	 * This is the method to set keyword for the KeywordNode.
	 *
	 * @param String
	 *            set the keyword for the KeywordNode.
	 */

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * This is the method to get list of keywordNode from the KeywordNode.
	 *
	 * @return List return the list of keywordNode from the KeywordNode.
	 */

	public List<KeywordNode> getSubsequentKeywords() {
		return subsequentKeywords;
	}

	/**
	 * This is the method to set list of keywordNode for the KeywordNode.
	 *
	 * @param List
	 *            set the list of keywordNode for the KeywordNode.
	 */

	private void setSubsequentKeywords(List<KeywordNode> subsequentKeywords) {
		this.subsequentKeywords = subsequentKeywords;
	}

	/**
	 * This is the method to add in keywordNode into list
	 *
	 * @param KeywordNode
	 *            add the keyword node into the list
	 */

	public void addSubsequentKeywords(KeywordNode node) {

		if (subsequentKeywords != null) {
			subsequentKeywords.add(node);
		}
	}

	/**
	 *
	 * This is to generate the hash code from the KeywordNode
	 *
	 * @return int The hash code generated.
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((keyword == null) ? 0 : keyword.hashCode());
		return result;
	}

	/**
	 *
	 * Compare between 2 keywordNode whether they are the same or not.
	 *
	 * @param Object
	 *            The parsed in object that need to be compared
	 * @return boolean return true if both keywordNode are the same, else false.
	 */

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof String) {
			String other = (String) obj;
			if (keyword == null) {
				if (other != null)
					return false;
			} else if (!keyword.equals(other))
				return false;
		}
		return true;
	}
}
