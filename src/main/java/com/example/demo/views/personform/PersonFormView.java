package com.example.demo.views.personform;
import com.example.demo.data.model.Miejsce;
import com.example.demo.data.repository.MiejsceRepository;
import com.example.demo.data.repository.ZamowienieRepository;
import com.example.demo.data.model.Zamowienie;
import com.example.demo.data.service.InitService;
import com.example.demo.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Zamów bilet online")
@Route(value = "zamow", layout = MainLayout.class)
@Uses(Icon.class)
public class PersonFormView extends Div {
    @Autowired
    private ZamowienieRepository zamowienieRepository;
    @Autowired
    private MiejsceRepository miejsceRepository;
    private TextField name = new TextField("Imie");
    private TextField lastName = new TextField("Nazwisko");
    private EmailField email = new EmailField("Adres email");
    private TextField phoneNumber = new TextField("Numer telefonu");
    private Button cancel = new Button("Anuluj");
    private Button save = new Button("Zarezerwuj");
    com.vaadin.flow.component.checkbox.Checkbox nextToWindow = new Checkbox("Miejsce przy oknie");
    private ComboBox<Integer> persons = new ComboBox("Ilość osób");
    private Binder<Zamowienie> binder = new Binder<>(Zamowienie.class);
    //private Miejsce seat = new Miejsce();
    public PersonFormView(ZamowienieRepository zamowienieRepository) {
        addClassName("person-form-view");
        add(createTitle());
        add(createFormLayout());
        persons.setItems(1,2,3);
        add(createButtonLayout());
        binder.bindInstanceFields(this);
        clearForm();
        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {
            if (checkIfNameAlreadyExists()) {//jeśli na to nazwisko już zarezerwowano
                clearForm();
                error("Zarezerwowano bilety na to nazwisko!");
            }
            else if (persons.getValue().equals(0)) { //jeśli nie wybrano liczby osob
                clearForm();
                error("Błąd! Nie wybrano liczby osób!");
            }
            else if (persons.getValue().equals(2) && nextToWindow.getValue().equals(true)) {//jeśli są 2 osoby i chca przy oknie obok siebie siedziec
                Long idM = miejsceRepository.findAll().stream().map(Miejsce::getIdMiejsce).filter(p-> findForTwoWithWindow(p) && (findForTwoWithWindow(p+1) || findForTwoWithWindow(p-1))).findFirst().get();
                    if ((miejsceRepository.findById(idM).filter(p->p.getLocation().equals(InitService.Location.PRZY_LEWYM_OKNIE)).isPresent()) && miejsceRepository.findById(idM+1).filter(p->p.getStatus().equals(InitService.Status.WOLNE)).isPresent()){
                        reserveTwoSeats(idM,0);
                    }
                    else if ((miejsceRepository.findById(idM).filter(p->p.getLocation().equals(InitService.Location.PRZY_PRAWYM_OKNIE)).isPresent()) && miejsceRepository.findById(idM-1).filter(p->p.getStatus().equals(InitService.Status.WOLNE)).isPresent()){
                        reserveTwoSeats(idM,1);
                    }
                    else {error("Brak dostępnych miejsc o podanych warunkach!");
                    }
            }
            else if (persons.getValue().equals(2) && nextToWindow.getValue().equals(false)) {//jesli sa 2 osoby i nie chca miejsca przy oknie i chca obok siebie siedziec
                Long idM = miejsceRepository.findAll().stream().map(Miejsce::getIdMiejsce).filter(p-> findForTwoNoWindow(p) && (findForTwoNoWindow(p+1) || findForTwoWithWindow(p-1))).findFirst().get();
                if ((miejsceRepository.findById(idM).filter(p->p.getLocation().equals(InitService.Location.NA_SRODKU)).isPresent()) && miejsceRepository.findById(idM+1).filter(p->p.getStatus().equals(InitService.Status.WOLNE) && p.getLocation().equals(InitService.Location.NA_SRODKU)).isPresent()) {
                    reserveTwoSeats(idM,0);
                }
                else if ((miejsceRepository.findById(idM).filter(p->p.getLocation().equals(InitService.Location.NA_SRODKU)).isPresent()) && miejsceRepository.findById(idM-1).filter(p->p.getStatus().equals(InitService.Status.WOLNE) && p.getLocation().equals(InitService.Location.NA_SRODKU)).isPresent()){
                    reserveTwoSeats(idM,1);
                }
                else {error("nie ma miejsca");}
            }
            else if(persons.getValue().equals(1)){ //jesli tylko jedna osoba
                Long findMatch = null;
                if(nextToWindow.getValue().equals(false)) { //jesli nie chce miejsca przy oknie
                    findMatch = miejsceRepository.findAll().stream().filter(p -> p.getStatus().equals(InitService.Status.WOLNE) && p.getLocation().equals(InitService.Location.NA_SRODKU)).map(Miejsce::getIdMiejsce).findFirst().get();
                }
                else { //jesli chce miejsce przy oknie
                    findMatch = miejsceRepository.findAll().stream().filter(p -> p.getStatus().equals(InitService.Status.WOLNE) && (p.getLocation().equals(InitService.Location.PRZY_LEWYM_OKNIE) || p.getLocation().equals(InitService.Location.PRZY_PRAWYM_OKNIE))).map(miejsceRepository -> miejsceRepository.getIdMiejsce()).findFirst().get();
                }
                if (!(findMatch == null)){ //jesli znaleziono
                    reserveOneSeat(findMatch);
                }
                else if (findMatch==null){
                    error("Błąd");
                    clearForm();
                }
            }
            else {error("Brak miejsc o podanych warunkach!");}
        });
    }

    //pomocnicze
    private void error(String text){
        Notification notification = Notification.show(text, 5000, Notification.Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        clearForm();
    }
    private void success(String text){
        Notification notification = Notification.show(text, 5000, Notification.Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
    private boolean findForTwoNoWindow(Long test){
        return miejsceRepository.findById(test).stream().filter(p->p.getStatus().equals(InitService.Status.WOLNE)&&(p.getLocation().equals(InitService.Location.NA_SRODKU))).findFirst().isPresent();}
    private boolean findForTwoWithWindow(Long test){
        return miejsceRepository.findById(test).stream().filter(p->p.getStatus().equals(InitService.Status.WOLNE)&&(p.getLocation().equals(InitService.Location.PRZY_PRAWYM_OKNIE)||p.getLocation().equals(InitService.Location.PRZY_LEWYM_OKNIE))).findFirst().isPresent();}
    private boolean checkIfNameAlreadyExists(){
        String imie = name.getValue();
        String nazwisko = lastName.getValue();
        return zamowienieRepository.findAll().stream().anyMatch(a->a.getName().equals(imie) && a.getLastName().equals(nazwisko));}

    private void clearForm() {
        binder.setBean(new Zamowienie());}

    private Component createTitle() {
        return new H3("Twoje dane");}

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        email.setErrorMessage("Wprowadź poprawny adres e-mail");
        formLayout.add(name, lastName, email, phoneNumber,persons, nextToWindow);
        return formLayout;
    }
    private void reserveOneSeat(Long seat){
        Miejsce miejsce = miejsceRepository.findById(seat).orElse(null);
        Zamowienie nowe  = binder.getBean();
        nowe.setIdMiejsce(seat+"");
        zamowienieRepository.save(binder.getBean());
        zamowienieRepository.save(nowe);
        binder.setBean(new Zamowienie());
        Miejsce X = miejsceRepository.findById(seat).orElseThrow();
        X.setStatus(InitService.Status.ZAJETE);
        X.setIdZamowienia(nowe);
        miejsceRepository.save(miejsce);
        miejsceRepository.save(X);
        success("Zamówienie przyjęto! Numer miejsca: " + seat);
    }
    private void reserveTwoSeats(Long idM,int what){
        Long idX;
        if (what==0)
        {
            idX=idM+1;
        }
        else{
            idX=idM-1;
        }
        Miejsce miejsce = miejsceRepository.findById(idM).orElse(null);
        Zamowienie nowe  = binder.getBean();
        nowe.setIdMiejsce(idM+"," +idX);
        zamowienieRepository.save(binder.getBean());
        zamowienieRepository.save(nowe);
        binder.setBean(new Zamowienie());
        Miejsce X = miejsceRepository.findById(idM).orElseThrow();
        Miejsce X2 = miejsceRepository.findById(idX).orElseThrow();
        X.setStatus(InitService.Status.ZAJETE);
        X.setIdZamowienia(nowe);
        X2.setStatus(InitService.Status.ZAJETE);
        X2.setIdZamowienia(nowe);
        miejsceRepository.save(miejsce);
        miejsceRepository.save(X);
        miejsceRepository.save(X2);
        success("Zamówienie przyjęto! Numery miejsc: " +idM+ " oraz " +(idX));
    }
    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        return buttonLayout;
    }
}