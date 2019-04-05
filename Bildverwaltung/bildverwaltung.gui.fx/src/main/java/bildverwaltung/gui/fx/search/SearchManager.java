package bildverwaltung.gui.fx.search;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import bildverwaltung.dao.entity.UUIDBase;
import bildverwaltung.dao.helper.ComparisonMode;
import bildverwaltung.dao.helper.DataFilter;
import bildverwaltung.gui.fx.util.TranslatedValue;
import bildverwaltung.gui.fx.util.ValueProxy;
import bildverwaltung.localisation.Translator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SearchManager<E extends UUIDBase> {

	private List<SearchCategory<E>> cathegories = new LinkedList<>();
	private SearchRenderer renderer;
	private Parent guiElement = null;
	private final Class<E> entityClass;
	private Translator translator;

	public SearchManager(List<SearchCategory<E>> cathegories, Class<E> entityClass, Translator translator) {
		this(cathegories, new DefaultSearchRenderer(), entityClass, translator);
	}

	public SearchManager(List<SearchCategory<E>> cathegories, SearchRenderer renderer, Class<E> entityClass,
			Translator translator) {
		super();
		this.cathegories = cathegories;
		if (renderer == null) {
			throw new IllegalArgumentException("No renderer was specified");
		}
		this.renderer = renderer;
		this.entityClass = entityClass;
		if (translator == null) {
			throw new IllegalArgumentException("No translater was supplied");
		}
		this.translator = translator;
		this.getGuiElement();
		this.reset();
	}

	public Parent getGuiElement() {
		if (guiElement == null) {
			renderer.setTranslator(translator);
			renderer.beginSearch();
			for (SearchCategory<E> cathegory : cathegories) {
				cathegory.render(renderer);
			}
			renderer.endSearch();
			guiElement = renderer.fetchResult();
		}
		return guiElement;
	}

	public DataFilter<E> toFilter() {
		DataFilter<E> res = new DataFilter<>(entityClass);
		for (SearchCategory<E> cath : cathegories) {
			cath.addToFilterList(res);
		}
		return res;
	}

	public void reset() {
		for (SearchCategory<E> cath : cathegories) {
			cath.reset();
		}
	}

	private static class DefaultSearchRenderer implements SearchRenderer {

		private Translator translator;

		private VBox searchPane = null;
		private TitledPane currentCategory = null;
		private GridPane contentGrid = new GridPane();
		private int currentLine = -1;
		private ChoiceBox<TranslatedValue<ComparisonMode>> comparisonMode;
		private SearchEntry<?, ?> currentEntry;
		private boolean hasCaption = false;
		private boolean hasComparisonMode = false;
		private boolean searchIsComplete = false;

		@Override
		public void beginSearch() {
			searchPane = new VBox();
			searchIsComplete = false;
		}

		@Override
		public void endSearch() {
			searchIsComplete = true;
		}

		@Override
		public <E extends UUIDBase> Parent fetchResult() {
			if (searchIsComplete) {
				return searchPane;
			} else {
				throw new IllegalStateException("Rendering process still incomplete");
			}
		}

		@Override
		public void beginCathegory(String name) {
			currentCategory = new TitledPane();
			currentCategory.setText(name);
			currentCategory.setStyle("-fx-box-border: transparent;");
			currentLine = -1;
			contentGrid = new GridPane();
			contentGrid.setPadding(new Insets(5d, 10d, 5d, 10d));
			contentGrid.setHgap(10d);
			contentGrid.setVgap(5d);
			contentGrid.getColumnConstraints()
					.add(new ColumnConstraints(50d, 50d, 50d, Priority.NEVER, HPos.LEFT, true));
			contentGrid.getColumnConstraints()
					.add(new ColumnConstraints(100d, 100d, 100d, Priority.NEVER, HPos.LEFT, true));
			contentGrid.getColumnConstraints()
					.add(new ColumnConstraints(100d, 100d, Double.MAX_VALUE, Priority.ALWAYS, HPos.LEFT, true));
			currentCategory.setContent(contentGrid);
		}

		@Override
		public void endCathegory() {
			searchPane.getChildren().add(currentCategory);
			currentCategory = null;
			contentGrid = null;

		}

		@Override
		public void beginSearchEntry(SearchEntry<?, ?> entry) {
			hasCaption = false;
			hasComparisonMode = false;
			this.currentEntry = entry;
			newLine();
		}

		@Override
		public void putSearchFieldLabel(String name) {
			if (hasCaption) {
				throw new IllegalArgumentException("Field has a caption already");
			}
			Label l = new Label(name);
			contentGrid.add(l, 0, currentLine);
			hasCaption = true;
		}

		@Override
		public ValueProxy<ComparisonMode> putCompairMode(ComparisonMode ... values) {
			if (hasComparisonMode) {
				throw new IllegalArgumentException("Field has a comparisonMode already");
			}
			ObservableList<TranslatedValue<ComparisonMode>> vals = FXCollections.observableArrayList();
			Arrays.sort(values);
			for (ComparisonMode m : values) {
				vals.add(new TranslatedValue<ComparisonMode>(getTranslator(), "itemLabelGenericSearch" + m.name(), m));
			}
			comparisonMode = new ChoiceBox<>(vals);
			comparisonMode.setMaxWidth(Double.MAX_VALUE);
			contentGrid.add(comparisonMode, 1, currentLine);
			hasComparisonMode = true;
			comparisonMode.valueProperty();

			return new ComparisonModeProxy(comparisonMode, vals);
		}

		@Override
		public void putInputField(Node value) {
			contentGrid.add(value, 2, currentLine);
		}

		@Override
		public void newLine() {
			currentLine++;

		}

		@Override
		public void endEntry() {
			if (!hasCaption || !hasComparisonMode) {
				throw new IllegalArgumentException("Field is incomplete");
			}
			comparisonMode.getSelectionModel().selectedItemProperty()
					.addListener(new CompModeChangeListener(currentEntry));
			comparisonMode.getSelectionModel().selectFirst();
		}

		private static class CompModeChangeListener implements ChangeListener<TranslatedValue<ComparisonMode>> {

			private final SearchEntry<?, ?> entry;

			public CompModeChangeListener(SearchEntry<?, ?> entry) {
				super();
				this.entry = entry;
			}

			@Override
			public void changed(ObservableValue<? extends TranslatedValue<ComparisonMode>> observable,
					TranslatedValue<ComparisonMode> oldValue, TranslatedValue<ComparisonMode> newValue) {
				entry.handleComparisonModeChange(oldValue != null ? oldValue.getValue() : null,
						newValue != null ? newValue.getValue() : null);
			}
		}

		private static class ComparisonModeProxy implements ValueProxy<ComparisonMode> {

			private final ChoiceBox<TranslatedValue<ComparisonMode>> comparisonMode;
			private final ObservableList<TranslatedValue<ComparisonMode>> items;

			public ComparisonModeProxy(ChoiceBox<TranslatedValue<ComparisonMode>> comparisonMode,
					ObservableList<TranslatedValue<ComparisonMode>> items) {
				super();
				this.comparisonMode = comparisonMode;
				this.items = items;
			}

			@Override
			public ComparisonMode get() {
				return comparisonMode.getValue().getValue();
			}

			@Override
			public void set(ComparisonMode obj) {
				if (obj != null) {
					for (TranslatedValue<ComparisonMode> t : items) {
						if (obj.equals(t.getValue())) {
							comparisonMode.setValue(t);
							// comparisonMode.getSelectionModel().select(t);
							return;
						}
					}
				}
				comparisonMode.getSelectionModel().selectFirst();

			}

		}

		@Override
		public Translator getTranslator() {
			return translator;
		}

		@Override
		public void setTranslator(Translator translator) {
			this.translator = translator;
		}
	}

}
