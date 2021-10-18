import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatButtonModule } from "@angular/material/button";
import { MatSidenavModule } from "@angular/material/sidenav";
import { MatListModule } from "@angular/material/list";
import { MatDividerModule } from "@angular/material/divider";
import { MatIconModule } from "@angular/material/icon";
import { SearchComponent } from './search/search.component';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatDialogModule } from "@angular/material/dialog";
import { MatSelectModule } from '@angular/material/select';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatAutocompleteModule } from "@angular/material/autocomplete";
import { MatProgressBarModule } from "@angular/material/progress-bar";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatBottomSheetModule } from "@angular/material/bottom-sheet";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatTabsModule } from "@angular/material/tabs";
import { MatTooltipModule } from "@angular/material/tooltip";
import { MatMenuModule } from "@angular/material/menu";
import { NgxChartsModule } from "@swimlane/ngx-charts";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { FlexLayoutModule } from "@angular/flex-layout";
import { FilterPipe } from './pipes/filter.pipe';
import { MatChipsModule } from "@angular/material/chips";
import { MatRadioModule } from "@angular/material/radio";
import { SortPipe } from './pipes/sort.pipe';
import { MatExpansionModule } from "@angular/material/expansion";

@NgModule({
  declarations: [SearchComponent, FilterPipe, SortPipe],
  providers: [FilterPipe],
  imports: [
    CommonModule,
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatListModule,
    MatDividerModule,
    MatIconModule,
    MatTableModule,
    MatCardModule,
    MatDialogModule,
    MatSelectModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatAutocompleteModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatDividerModule,
    MatBottomSheetModule,
    MatSnackBarModule,
    MatTabsModule,
    MatTooltipModule,
    MatMenuModule,
    NgxChartsModule,
    MatPaginatorModule,
    MatCheckboxModule,
    MatTooltipModule,
    FlexLayoutModule,
    MatChipsModule,
    MatCheckboxModule,
    MatRadioModule,
    MatExpansionModule
  ],
    exports: [
        MatToolbarModule,
        MatButtonModule,
        MatSidenavModule,
        MatListModule,
        MatDividerModule,
        MatIconModule,
        MatTableModule,
        MatCardModule,
        MatDialogModule,
        MatSelectModule,
        FormsModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatAutocompleteModule,
        MatProgressBarModule,
        MatDividerModule,
        SearchComponent,
        MatProgressSpinnerModule,
        MatBottomSheetModule,
        MatSnackBarModule,
        MatTabsModule,
        MatTooltipModule,
        MatMenuModule,
        NgxChartsModule,
        MatPaginatorModule,
        MatCheckboxModule,
        MatTooltipModule,
        MatChipsModule,
        FlexLayoutModule,
        MatCheckboxModule,
        FilterPipe,
        MatRadioModule,
        SortPipe,
        MatExpansionModule
    ]
})
export class SharedModule {
}
