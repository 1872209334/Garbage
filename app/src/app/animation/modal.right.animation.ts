import { Animation, PageTransition } from 'ionic-angular';

export class ModalRightAnimation extends PageTransition {

  public init() {
    const ele = this.leavingView.pageRef().nativeElement;
    const wrapper = new Animation(this.plt, ele.querySelector('.modal-wrapper'));
    const contentWrapper = new Animation(this.plt, ele.querySelector('.wrapper'));

    wrapper.beforeStyles({ 'transform': 'scale(0)', 'opacity': 1 });
    //wrapper.fromTo('transform', 'scale(1)', 'scale(5.0)');
    wrapper.fromTo('transform', 'translateX(0)', 'translateX(100%)');
    wrapper.fromTo('opacity', 1, 1);
    contentWrapper.fromTo('opacity', 1, 1);

    this.element(this.leavingView.pageRef())
        .add(contentWrapper)
        .add(wrapper)
        .easing('cubic-bezier(.25, .1, .25, 1)')
        .duration(230)
  }
}