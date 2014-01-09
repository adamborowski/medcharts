///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package pl.adamborowski.largeplotter;
//
//import java.awt.Cursor;
//import java.awt.Rectangle;
//import java.awt.event.KeyEvent;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.awt.event.MouseMotionListener;
//import java.awt.event.MouseWheelEvent;
//import java.awt.event.MouseWheelListener;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import pl.adamborowski.utils.KeyboardPressingDispatcher;
//
///**
// *
// * @author test
// */
//public class NavigationController {
//
//    private Cursor normalCurosr = new Cursor(Cursor.DEFAULT_CURSOR);
//    private Cursor moveCurosr = new Cursor(Cursor.MOVE_CURSOR);
//    private Cursor resizeLeftEdgeCurosr = new Cursor(Cursor.W_RESIZE_CURSOR);
//    private Cursor resizeRightEdgeCursor = new Cursor(Cursor.E_RESIZE_CURSOR);
//    private Rectangle selectionRectangle = new Rectangle(0, 0, 0, 0);
//
//    State getState() {
//        return state;
//    }
//
//    public enum State {
//
//        SELECTING, NORMAL, PANNING, DRAWING, MOVING, RESIZING
//    };
//    private State state = State.NORMAL;
//
//    private void setState(State state) {
//        this.state = state;
//    }
//    private SelectionItem activeItem = null;
//    private ArrayList<SelectionItem> selectedItems = new ArrayList<>();
//
//    public ArrayList<SelectionItem> getSelectedItems() {
//        return selectedItems;
//    }
//
//    public Rectangle getSelectionRectangle() {
//        return selectionRectangle;
//    }
//
//    public void deselectAll() {
//        for (SelectionItem i : selectedItems) {
//            i.setSelected(false);
//        }
//        selectedItems.clear();
//        if (activeItem != null) {
//            activeItem.setSelected(false);
//            activeItem = null;
//        }
//        renderer.repaint();
//    }
//    private int pressY;
//    private int pressX;
//    private int mouseDisplayX;
//    private int mouseDisplayY;
//    private long centerDataX;
//    private float centerDataY;
//    private int mouseOffsetX;
//    private int mouseOffsetY;
//    private long itemOffsetX; // przydatne do przeciągania czy rozciągania zaznaczeń
//    private boolean resisingRightEdge; // przydatne przy state=RESIZING
//
//    public int getMouseDisplayX() {
//        return mouseDisplayX;
//    }
//
//    public int getMouseDisplayY() {
//        return mouseDisplayY;
//    }
//
//    public long getMouseDataX() {
//        return converter.toDataX(mouseDisplayX);
//    }
//
//    public float getMouseDataY() {
//        return converter.toDataY(mouseDisplayY);
//    }
//    /**
//     *
//     * RENDERER ma navigationcontroller, plotter oraz selectionController
//     * (plotter korzysta z selectionControlera podanego w konstruktorze zeby
//     * wyrenderować kolekcję jego zaznaczeń) navigationController korzysta z
//     * selectionControllera, zeby mu władować nowe zaznaczenie albo usunąć...
//     * selectionController posiada obiekt GHOST, czyli ten nowy wlasnie
//     * zaznaczany...
//     *
//     */
//    private final Renderer renderer;
//    private final Plotter.SpaceConverter converter;
//    KeyboardPressingDispatcher kpd;
//
//    public NavigationController(Renderer renderer) {
//        this.renderer = renderer;
//        converter = renderer.getPlotter().convert();
//        renderer.addMouseListener(handler);
//        renderer.addMouseMotionListener(handler);
//        renderer.addMouseWheelListener(handler);
//        renderer.setFocusable(true);
//        this.kpd = new KeyboardPressingDispatcher(renderer, 50);
//        kpd.addKey(KeyEvent.VK_LEFT);
//        kpd.addKey(KeyEvent.VK_RIGHT);
//        kpd.addKey(KeyEvent.VK_UP);
//        kpd.addKey(KeyEvent.VK_DOWN);
//        kpd.setKeyboardListener(listener);
//    }
//    KeyboardPressingDispatcher.KeyboardListener listener = new KeyboardPressingDispatcher.KeyboardListener() {
//        float startSpeed = 10;
//        float increment = 1f;
//        float speedX = startSpeed;
//        float speedY = startSpeed;
//
//        @Override
//        public void keyPressing(KeyboardPressingDispatcher kpd) {
//            if (renderer.getDriver() == null) {
//                return;
//            }
//            int dx = 0;
//            int dy = 0;
//            if (kpd.isKeyDown(KeyEvent.VK_LEFT)) {
//                dx--;
//            }
//            if (kpd.isKeyDown(KeyEvent.VK_RIGHT)) {
//                dx++;
//            }
//            if (kpd.isKeyDown(KeyEvent.VK_UP)) {
//                dy--;
//            }
//            if (kpd.isKeyDown(KeyEvent.VK_DOWN)) {
//                dy++;
//            }
//            if (dx != 0) {
//                renderer.setDataX(renderer.getDataX() + converter.toDataWidth(speedX * dx));
//                speedX += increment;
//            } else {
//                speedX = startSpeed;
//            }
//            if (dy != 0) {
//                renderer.setDataY(renderer.getDataY() + converter.toDataHeight(speedY * dy));
//                speedY += increment;
//            } else {
//                speedY = startSpeed;
//            }
//            renderer.forcePlotterUpdate();
//            handler.mouseDragged(new MouseEvent(renderer, 0, new Date().getTime(), 0, renderer.getMousePosition().x,
//                    renderer.getMousePosition().y, 0, false));
//        }
//
//        @Override
//        public void keyReleased(KeyboardPressingDispatcher kpd) {
//            if (renderer.getDriver() == null) {
//                return;
//            }
//            if (kpd.isKeyUp(KeyEvent.VK_LEFT) && kpd.isKeyUp(KeyEvent.VK_RIGHT)) {
//                speedX = startSpeed;
//            }
//            if (kpd.isKeyUp(KeyEvent.VK_UP) && kpd.isKeyUp(KeyEvent.VK_DOWN)) {
//                speedY = startSpeed;
//            }
//        }
//
//        @Override
//        public void keyPressed(KeyboardPressingDispatcher kpd) {
//        }
//    };
//    private MouseHandler handler = new MouseHandler();
//
//    private class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener {
//
//        private boolean control;
//        private boolean shift;
//
//        @Override
//        public void mouseClicked(MouseEvent e) {
//            mouseDisplayX = e.getX();
//            mouseDisplayY = e.getY();
//        }
//
//        @Override
//        public void mousePressed(MouseEvent e) {
//            if (renderer.getDriver() == null) {
//                return;
//            }
//            mouseDisplayX = e.getX();
//            mouseDisplayY = e.getY();
//
//            if (state != State.NORMAL) {
//                return;
//            }
//            pressX = e.getX();
//            pressY = e.getY();
//            long dataUnderMouse = getMouseDataX();
//            renderer.requestFocus();
//            control = e.isControlDown();
//            shift = e.isShiftDown();
//            mouseOffsetX = e.getX() - renderer.getWidth() / 2;
//            mouseOffsetY = e.getY() - renderer.getHeight() / 2;
//            centerDataX = converter.toDataX(renderer.getWidth() / 2);
//            centerDataY = converter.toDataY(renderer.getHeight() / 2);
//            if (e.getButton() == MouseEvent.BUTTON3) {
//                setState(State.PANNING);
//            } else if (e.getButton() == MouseEvent.BUTTON1) {
//                SelectionItem itemUnderMouse = renderer.getSelectionController().getItemContainingData(dataUnderMouse);
//                if (e.isShiftDown()) {
//                    deselectAll();
//                    renderer.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
//                    setState(State.SELECTING);
//                } else if (e.isControlDown()) {
//                    if (itemUnderMouse != null) {
//                        if (itemUnderMouse.isSelected()) {
//                            selectedItems.remove(itemUnderMouse);
//                            itemUnderMouse.setSelected(false);
//                        } else {
//
//                            selectedItems.add(itemUnderMouse);
//                            itemUnderMouse.setSelected(true);
//                        }
//                        updateSelections();
//                        renderer.repaint();
//                    }
//                } else {
//                    //pytanie czy my mamy kursor na jakimś zaznaczeniu
//                    renderer.getSelectionController().deselectAll();
//
//                    if (itemUnderMouse == null) {
//                        //zaczyna się rysowanie
//                        setState(State.DRAWING);
//                        //nowy ghost
//                        renderer.getSelectionController().
//                                createGhost(converter.toDataXAndFixToFitIntervalAndPhaseShift(e.getX() - 1),
//                                converter.toDataXAndFixToFitIntervalAndPhaseShift(e.getX()));
//                        renderer.repaint();
//                    } else {
//                        deselectAll();
//                        activeItem = itemUnderMouse;
//                        activeItem.setSelected(true);
//                        selectedItems.add(activeItem);
//                        activeItem.setState(SelectionController.ItemState.DOWN);
//                        //kalkuluj, czy myszka jest x pixeli od lewej lub prawej strony!!
//                        int itemDisplayStart = (int) converter.toDisplayX(activeItem.getStart());
//                        int itemDisplayEnd = (int) converter.toDisplayX(activeItem.getEnd());
//                        int itemStartResizeBound = itemDisplayStart + Plotter.RESIZE_GAP_HORIZONTAL;
//                        int itemEndResizeBound = itemDisplayEnd - Plotter.RESIZE_GAP_HORIZONTAL;
//                        boolean mouseHCondition = mouseDisplayY <= Plotter.RESIZE_GAP_VERTICAL || mouseDisplayY >= renderer.getHeight() - Plotter.RESIZE_GAP_VERTICAL;
//                        activeItem.fixBounds(); // teraz można bezpiecznie naprawić start/end (trzeba ręcznie, bo niemożna przy przemieszczaniu czy rysowaniu)
//                        if (mouseDisplayX <= itemStartResizeBound && mouseHCondition) {
//                            resisingRightEdge = false;
//                            itemOffsetX = dataUnderMouse - activeItem.getStart();
//                            //rozciąganie z lewej
//                            setState(State.RESIZING);
//                        } else if (mouseDisplayX >= itemEndResizeBound && mouseHCondition) {
//                            resisingRightEdge = true;
//                            itemOffsetX = dataUnderMouse - activeItem.getEnd();
//                            setState(State.RESIZING);
//                        } else {
//                            setState(State.MOVING);
//                            itemOffsetX = dataUnderMouse - activeItem.getStart();
//                            mouseDragged(e);
//                        }
//                        renderer.repaint();
//                    }
//                }
//
//            }
//        }
//
//        @Override
//        public void mouseReleased(MouseEvent e) {
//            mouseDisplayX = e.getX();
//            mouseDisplayY = e.getY();
//            if (renderer.getDriver() == null) {
//                return;
//            }
//            switch (state) {
//                case SELECTING:
//                    setState(State.NORMAL);
//                    selectionRectangle = new Rectangle(0, 0, 0, 0);
//                    renderer.repaint();
//                    break;
//                case NORMAL:
//                    break;
//                case PANNING:
//                    break;
//                case DRAWING:
//                    deselectAll();
//                    activeItem = renderer.getSelectionController().getGhost();
//                    activeItem.setSelected(true);
//                    selectedItems.add(activeItem);
//                    renderer.getSelectionController().addGhostToItems();
//                    renderer.repaint();
//                    break;
//                case MOVING:
//                case RESIZING:
//                    activeItem.setState(SelectionController.ItemState.HOVERED);
//                    renderer.repaint();
//                    break;
//                default:
//                    throw new AssertionError(state.name());
//
//            }
//            setState(State.NORMAL);
//            mouseMoved(e);
//        }
//
//        @Override
//        public void mouseEntered(MouseEvent e) {
//        }
//
//        @Override
//        public void mouseExited(MouseEvent e) {
//        }
//
//        @Override
//        public void mouseDragged(MouseEvent e) {
//            mouseDisplayX = e.getX();
//            mouseDisplayY = e.getY();
//
//            control = e.isControlDown();
//            shift = e.isShiftDown();
//            if (renderer.getDriver() == null) {
//                return;
//            }
//            long dataUnderMouse = getMouseDataX();
//            switch (state) {
//                case SELECTING:
//                    selectionRectangle = new Rectangle(pressX, pressY, mouseDisplayX - pressX, mouseDisplayY - pressY);
//                    if (selectionRectangle.width < 0) {
//                        selectionRectangle.x += selectionRectangle.width;
//                        selectionRectangle.width *= -1;
//                    }
//                    if (selectionRectangle.height < 0) {
//                        selectionRectangle.y += selectionRectangle.height;
//                        selectionRectangle.height *= -1;
//                    }
//                    deselectAll();
//                    selectedItems = renderer.getSelectionController().getItemsInRange(
//                            converter.toDataX(selectionRectangle.x),
//                            converter.toDataX(selectionRectangle.x + selectionRectangle.width));
//                    updateSelections();
//                    renderer.repaint();
//                    break;
//                case NORMAL:
//                    break;
//                case PANNING:
//                    if (renderer.getControlHorizontal() && !control) {
//                        renderer.setDataX(centerDataX - converter.toDataWidth(e.getX() - renderer.getWidth() / 2 - mouseOffsetX));
//                    }
//                    if (renderer.getControlVertical() && !shift) {
//                        renderer.setDataY(centerDataY - converter.toDataHeight(e.getY() - renderer.getHeight() / 2 - mouseOffsetY));
//                    }
//                    break;
//                case DRAWING:
//                    renderer.repaint();
//                    renderer.getSelectionController().getGhost().tryResize(true, converter.fixDataToFitIntervalAndPhaseShift(dataUnderMouse));
//                    break;
//                case MOVING:
//                    //                activeItem.setPosition(converter.fixDataToFitIntervalAndPhaseShift(dataUnderMouse - itemOffsetX));
//                    activeItem.tryMove(converter.fixDataToFitIntervalAndPhaseShift(dataUnderMouse - itemOffsetX), dataUnderMouse);
//                    renderer.repaint();
//                    break;
//                case RESIZING:
//                    activeItem.tryResize(resisingRightEdge, converter.fixDataToFitIntervalAndPhaseShift(dataUnderMouse - itemOffsetX));
//                    renderer.repaint();
//                    break;
//            }
//        }
//
//        @Override
//        public void mouseMoved(MouseEvent e) {
//            mouseDisplayX = e.getX();
//            mouseDisplayY = e.getY();
//            if (renderer.getDriver() == null) {
//                return;
//            }
//            renderer.setCursor(normalCurosr);
//            long dataUnderMouse = getMouseDataX();
//            for (SelectionItem item : renderer.getSelectionController().getItems()) {
//                item.setState(SelectionController.ItemState.NORMAL);
//            }
//            if (state == State.NORMAL) {
//                if (e.isShiftDown()) {
//                    return;
//                }
//                SelectionItem item = renderer.getSelectionController().getItemContainingData(dataUnderMouse);
//                if (item != null) {
//
//                    SelectionItem hoveredItem = item;
//                    hoveredItem.setState(SelectionController.ItemState.HOVERED);
//                    //renderer.setCursor(moveCurosr);
//                    int itemDisplayStart = (int) converter.toDisplayX(hoveredItem.getStart());
//                    int itemDisplayEnd = (int) converter.toDisplayX(hoveredItem.getEnd());
//                    int itemStartResizeBound = itemDisplayStart + Plotter.RESIZE_GAP_HORIZONTAL;
//                    int itemEndResizeBound = itemDisplayEnd - Plotter.RESIZE_GAP_HORIZONTAL;
//                    boolean mouseHCondition = mouseDisplayY <= Plotter.RESIZE_GAP_VERTICAL || mouseDisplayY >= renderer.getHeight() - Plotter.RESIZE_GAP_VERTICAL;
//                    if (mouseDisplayX <= itemStartResizeBound && mouseHCondition) {
//                        renderer.setCursor(resizeLeftEdgeCurosr);
//                    } else if (mouseDisplayX >= itemEndResizeBound && mouseHCondition) {
//                        renderer.setCursor(resizeRightEdgeCursor);
//                    } else {
//                        renderer.setCursor(moveCurosr);
//                    }
//                } else {
//                    renderer.setCursor(normalCurosr);
//                }
//            }
//            renderer.repaint();
//        }
//
//        @Override
//        public void mouseWheelMoved(final MouseWheelEvent e) {
//            mouseDisplayX = e.getX();
//            mouseDisplayY = e.getY();
//            control = e.isControlDown();
//            shift = e.isShiftDown();
//            if (renderer.getDriver() == null) {
//                return;
//            }
//            if (!shift && !control) {
//                //dodawanie o wielość jedego labela
//                renderer.setDataX(renderer.getDataX() + converter.toDataWidth(renderer.getPlotter().getLabelWidth() * 1.1f * (e.getWheelRotation() > 0 ? -1 : 1)));
//            } else {
//                //tutaj dodaj zooma i doprowadź, żeby punkt pod myszą był nadal pod myszą!!
//                float scaleX = renderer.getScaleX();
//                float scaleY = renderer.getScaleY();
//                long xDataUnderCursor = converter.toDataX(e.getX());
//                float yDataUnderCursor = converter.toDataY(e.getY());
//                final float fraction = 1.5f;
//                try {
//                    if (shift) {
//                        renderer.setScaleX(scaleX * (e.getWheelRotation() > 0 ? fraction : 1 / fraction));
//                    }
//                    if (control) {
//                        renderer.setScaleY(scaleY * (e.getWheelRotation() > 0 ? fraction : 1 / fraction));
//                    }
//                    renderer.forcePlotterUpdate();
//                    long newXDataUnderCursor = converter.toDataX(e.getX());
//                    float newYDataUnderCursor = converter.toDataY(e.getY());
//                    long xDiff = newXDataUnderCursor - xDataUnderCursor;
//                    float yDiff = newYDataUnderCursor - yDataUnderCursor;
//                    renderer.setDataX(renderer.getDataX() - xDiff);
//                    renderer.setDataY(renderer.getDataY() - yDiff);
//                } catch (IllegalArgumentException error) {
//                    renderer.setScaleX(scaleX);
//                    renderer.setScaleY(scaleY);
//                }
//            }
//            if (state == State.DRAWING) {
//                renderer.getSelectionController().getGhost().setEnd(converter.toDataXAndFixToFitIntervalAndPhaseShift(e.getX()));
//                renderer.repaint();
//            }
//            //trzeba na siłę wykonać obliczenia potrzebne konwerterowi data-display
//            renderer.getPlotter().calculateRendererDependencies();
//            mouseDragged(e);
//        }
//    }
//
//    private void updateSelections() {
//        for (SelectionItem i : selectedItems) {
//            i.setSelected(true);
//        }
//        renderer.repaint();
//    }
//
//    public void deleteSelected() {
//        if (renderer.getDriver() == null) {
//            return;
//        }
//        if (state == State.NORMAL) {
//            for (SelectionItem i : selectedItems) {
//                renderer.getSelectionController().removeItem(i);
//            }
//            renderer.repaint();
//        }
//    }
//
//    public void joinSelected() {
//        if (selectedItems.size() < 2) {
//            return;
//        }
//        Collections.sort(selectedItems);
//        SelectionItem newItem = new SelectionItem(renderer.getSelectionController());
//        newItem.setStart(selectedItems.get(0).getStart());
//        newItem.setEnd(selectedItems.get(selectedItems.size() - 1).getEnd());
//        newItem.setState(SelectionController.ItemState.NORMAL);
//        ArrayList<SelectionItem> joinedItems = renderer.getSelectionController().getItemsInRange(newItem.getStart(), newItem.getEnd());
//
//        for (SelectionItem i : joinedItems) {
//            renderer.getSelectionController().removeItem(i);
//        }
//        renderer.getSelectionController().addItem(newItem);
//        selectedItems.clear();
//        selectedItems.add(newItem);
//        updateSelections();
//        renderer.repaint();
//    }
//
//    public void selectAll() {
//        if (renderer.getDriver() == null) {
//            return;
//        }
//        selectedItems = (ArrayList<SelectionItem>) renderer.getSelectionController().getItems().clone();
//        updateSelections();
//
//    }
//}
