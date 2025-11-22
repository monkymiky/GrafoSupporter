import { CommonModule } from "@angular/common";
import {
  Component,
  HostListener,
  OnInit,
  computed,
  inject,
  signal,
} from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { AuthService } from "../../core/services/auth.service";
import { LoginModalService } from "../../shared/components/login-modal/services/login-modal.service";

@Component({
  selector: "app-home",
  standalone: true,
  imports: [CommonModule],
  templateUrl: "./home.component.html",
  styleUrl: "./home.component.scss",
})
export class HomeComponent implements OnInit {
  readonly router = inject(Router);
  readonly route = inject(ActivatedRoute);
  readonly authService = inject(AuthService);
  readonly loginModalService = inject(LoginModalService);

  isHovering = false;
  goDownNow = false;
  imageIndex = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
  windowWidthBreakpoint = [1000, 768, 576, 480];
  currentWindowWidthBreakpoint = 0;

  ellipseWidthVw = signal<number>(125);
  ellipseLeftVw = computed(() => -(this.ellipseWidthVw() - 100) / 2);
  ellipseHeightVh = signal<number>(100);
  ellipseTopOffsetVh = signal<number>(-80);
  ellipseBottomOffsetVh = signal<number>(50);
  scene3dPerspective = computed(() => this.ellipseWidthVw() * 0.5);

  private adjustCarouselDependingOnWindowWidth() {
    let i = 0;
    while (window.innerWidth < this.windowWidthBreakpoint[i]) {
      this.currentWindowWidthBreakpoint = this.windowWidthBreakpoint[i];
      i++;
    }

    switch (this.currentWindowWidthBreakpoint) {
      case 1000:
        this.imageIndex = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
        this.panelAngle = 360 / this.imageIndex.length;
        this.ellipseWidthVw.set(150);
        this.ellipseHeightVh.set(100);
        this.ellipseTopOffsetVh.set(-80);
        this.ellipseBottomOffsetVh.set(50);
        break;
      case 768:
        this.imageIndex = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9];
        this.panelAngle = 360 / this.imageIndex.length;
        this.ellipseWidthVw.set(200);
        this.ellipseHeightVh.set(100);
        this.ellipseTopOffsetVh.set(-77);
        this.ellipseBottomOffsetVh.set(47);
        break;
      case 576:
        this.imageIndex = [0, 1, 2, 3, 4, 5, 6, 7];
        this.panelAngle = 360 / this.imageIndex.length;
        this.ellipseWidthVw.set(225);
        this.ellipseHeightVh.set(100);
        this.ellipseTopOffsetVh.set(-75);
        this.ellipseBottomOffsetVh.set(45);
        break;
      case 480:
        this.imageIndex = [0, 1, 2, 3, 4, 5, 6];
        this.panelAngle = 360 / this.imageIndex.length;
        this.ellipseWidthVw.set(250);
        this.ellipseHeightVh.set(100);
        this.ellipseTopOffsetVh.set(-72);
        this.ellipseBottomOffsetVh.set(42);
        break;
      default:
        this.imageIndex = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
        this.panelAngle = 360 / this.imageIndex.length;
        this.ellipseWidthVw.set(150);
        this.ellipseHeightVh.set(100);
        this.ellipseTopOffsetVh.set(-80);
        this.ellipseBottomOffsetVh.set(50);
        break;
    }
  }
  @HostListener("window:resize", ["$event"])
  onResize(event: Event) {
    this.adjustCarouselDependingOnWindowWidth();
  }
  play() {
    this.isHovering = true;
  }
  stop() {
    this.isHovering = false;
  }

  image: string = "assets/combination-image/scrittura-home3.png";

  panelAngle = 0;

  ngOnInit(): void {
    this.panelAngle = 360 / this.imageIndex.length;
    this.adjustCarouselDependingOnWindowWidth();

    this.route.queryParams.subscribe((params) => {
      if (params["token"]) {
        const authUser = {
          token: params["token"],
          email: params["email"] || "",
          name: params["name"] || "",
          pictureUrl: params["pictureUrl"] || "",
          userId: Number.parseInt(params["userId"] || "0", 10),
        };
        console.debug("Auth user from query params:", authUser);
        setTimeout(() => {
          // setTimeout is necessary to force the calling of these operations to be in the next angular call-back cicle to fix the error https://angular.dev/errors/NG0100
          this.authService.setUser(authUser);
          this.router.navigate(["/ricerca-combinazioni"], { replaceUrl: true });
        }, 0);
      }

      if (params["error"]) {
        console.error("Authentication error:", params["error"]);
      }
    });
  }

  openLogin(): void {
    this.loginModalService.open("login");
  }
  openSignup(): void {
    this.loginModalService.open("signup");
  }
}
