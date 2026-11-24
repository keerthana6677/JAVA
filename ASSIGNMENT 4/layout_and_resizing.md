# Task 3 Layout and Resizing

## Containment Tree
JFrame(BorderLayout) -> top JPanel(BorderLayout) -> title JLabel and search JPanel(FlowLayout); centre JScrollPane -> JTable; bottom JPanel(BorderLayout) -> navigation JPanel(FlowLayout) -> buttons and status JLabel.

## Layout Comparison
Nested BorderLayout/FlowLayout works without coordinate values and adapts naturally. GridBagLayout is more flexible for unequal-width grids but is harder to maintain. Absolute positioning resists window resizing and is unsuitable.

## Resize Checklist
- 800x600: controls visible, table scrollable.
- 1100x750: table fills centre and navigation remains clear.
- 1400x900: all regions expand smoothly.
